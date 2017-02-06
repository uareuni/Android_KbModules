package com.example.kbpark.kbbeacon;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.TextView;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;
import java.util.Collection;


/**
 *  RECO에서는 기기를 등록하면 sdk와 sample app이 제공된다.
 *  아래 코드는 sample app에서 monitoring과 ranging이 돌아가는 가장 기본적인 process를 한눈에 정리하기 위한 test 코드다.
 *  또한 아래 코드는 app 실행 상태에서의 동작이고, service를 사용한 background test는 곧 정리해서 update할 예정이다.
 */

public class MainActivity extends Activity implements RECOServiceConnectListener, RECOMonitoringListener, RECORangingListener {

    //This is a default proximity uuid of the RECO
    public static final String RECO_UUID = "24DDF411-8CF1-440C-87CD-E368DAF9C93E";

    /**
     * SCAN_RECO_ONLY:
     *
     * true일 경우 레코 비콘만 스캔하며, false일 경우 모든 비콘을 스캔합니다.
     * RECOBeaconManager 객체 생성 시 사용합니다.
     */
    public static final boolean SCAN_RECO_ONLY = true;

    /**
     * ENABLE_BACKGROUND_RANGING_TIMEOUT:
     *
     * 백그라운드 ranging timeout을 설정합니다.
     * true일 경우, 백그라운드에서 입장한 region에서 ranging이 실행 되었을 때, 10초 후 자동으로 정지합니다.
     * false일 경우, 계속 ranging을 실행합니다. (배터리 소모율에 영향을 끼칩니다.)
     * RECOBeaconManager 객체 생성 시 사용합니다.
     */
    public static final boolean ENABLE_BACKGROUND_RANGING_TIMEOUT = true;

    /**
     * DISCONTINUOUS_SCAN:
     *
     * 일부 안드로이드 기기에서 BLE 장치들을 스캔할 때, 한 번만 스캔 후 스캔하지 않는 버그(참고: http://code.google.com/p/android/issues/detail?id=65863)가 있습니다.
     * 해당 버그를 SDK에서 해결하기 위해, RECOBeaconManager에 setDiscontinuousScan() 메소드를 이용할 수 있습니다.
     * 해당 메소드는 기기에서 BLE 장치들을 스캔할 때(즉, ranging 시에), 연속적으로 계속 스캔할 것인지, 불연속적으로 스캔할 것인지 설정하는 것입니다.
     * 기본 값은 FALSE로 설정되어 있으며, 특정 장치에 대해 TRUE로 설정하시길 권장합니다.
     */
    public static final boolean DISCONTINUOUS_SCAN = false;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION = 10;

    public static final int BEACON_MAJOR = 501;
    public static final int FIRST_BEACON_MINOR = 6461;
    public static final int SECOND_BEACON_MINOR = 6462;
    public static final int THIRD_BEACON_MINOR = 6463;

    boolean mScanRecoOnly = true;
    boolean mEnableBackgroundTimeout = true;

    private long mScanPeriod = 1*1000L;  // scan 시간을 설정할 수 있습니다.
    private long mSleepPeriod = 5*1000L; // scan 후, 다음 scan 시작 전까지의 시간을 설정할 수 있습니다.
    private long mExpirationPeriod = 60*1000L; // region의 expiration 시간을 설정할 수 있습니다. 기본 값은 60초(1분) 입니다.

    RECOBeaconManager recoManager;
    ArrayList<RECOBeaconRegion> monitoringRegions;
    ArrayList<RECOBeaconRegion> rangingRegions;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Beacon 시작 \n");

        // 1. manager setting : (공통)
        recoManager = RECOBeaconManager.getInstance(this, mScanRecoOnly, mEnableBackgroundTimeout);
        recoManager.setMonitoringListener(this);

        recoManager.setRangingListener(this);

        // 2. bind() : (공통)
        recoManager.bind(this);
        ///////////////////// bind()가 불리면 바로 onServiceConnected() callback 불림 ////////////////////////////

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // manager unbind()
        try {
            recoManager.unbind();
        } catch (RemoteException re) {
            re.printStackTrace();
        }

        // moniroting beacons unbind()
        for(RECOBeaconRegion region : monitoringRegions) {
            try {
                recoManager.stopMonitoringForRegion(region); // 각 beacon 객체들 해제
            } catch (RemoteException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        // ranging beacons unbind()
        for(RECOBeaconRegion region : rangingRegions) {
            try{
                recoManager.stopRangingBeaconsInRegion(region);
            } catch (RemoteException | NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * ---------------------------- Service callbacks  ----------------------------------------
     */

    // 3. 연결 되었을때 제공되는 서비스 부분 : (공통)
    @Override
    public void onServiceConnect() {
        tv.append("연결 되었습니다. \n");

        /**
         * -------------------- monitoring test ---------------------------
         */

        monitoringRegions = new ArrayList<RECOBeaconRegion>();
        monitoringRegions.add(new RECOBeaconRegion(RECO_UUID, BEACON_MAJOR, FIRST_BEACON_MINOR, "1번 비콘"));
        monitoringRegions.add(new RECOBeaconRegion(RECO_UUID, BEACON_MAJOR, SECOND_BEACON_MINOR, "2번 비콘"));
        monitoringRegions.add(new RECOBeaconRegion(RECO_UUID, BEACON_MAJOR, THIRD_BEACON_MINOR, "3번 비콘"));

        recoManager.setScanPeriod(mScanPeriod);
        recoManager.setSleepPeriod(mSleepPeriod);

        try {
            for(RECOBeaconRegion region : monitoringRegions)
            {
                    region.setRegionExpirationTimeMillis(mExpirationPeriod);
                    recoManager.startMonitoringForRegion(region); // 4. monitoring 시작
            }
        } catch (RemoteException | NullPointerException e) {
            e.printStackTrace();
        }

        /**
         *  -------------------- ranging test ---------------------------
         */
        rangingRegions = new ArrayList<RECOBeaconRegion>();
        rangingRegions.add(new RECOBeaconRegion(RECO_UUID, BEACON_MAJOR, FIRST_BEACON_MINOR, "1번 비콘"));
        rangingRegions.add(new RECOBeaconRegion(RECO_UUID, BEACON_MAJOR, SECOND_BEACON_MINOR, "2번 비콘"));
        rangingRegions.add(new RECOBeaconRegion(RECO_UUID, BEACON_MAJOR, THIRD_BEACON_MINOR, "3번 비콘"));

        try {
            for(RECOBeaconRegion reco : rangingRegions)
            {
                recoManager.startRangingBeaconsInRegion(reco); // 4.  ranging 시작
            }

        }catch (RemoteException re) {
            re.printStackTrace();
        }

    }

    @Override
    public void onServiceFail(RECOErrorCode arg0) {
        // TODO Auto-generated method stub
        tv.append("Service 연결 실패.. \n");
    }


    /**
     *---------------------------- 1. Monitoring callbacks -------------------------------------
     */

    @Override
    public void didEnterRegion(RECOBeaconRegion recoBeaconRegion, Collection<RECOBeacon> collection) {
        // 1. region 입장시
        tv.append("region 입장. \n");
    }

    @Override
    public void didStartMonitoringForRegion(RECOBeaconRegion recoBeaconRegion) {
        // 2. region 입장 후 정상 실행 시
        tv.append("region 정상 실행! \n");
    }

    @Override
    public void didDetermineStateForRegion(RECOBeaconRegionState recoBeaconRegionState, RECOBeaconRegion recoBeaconRegion) {
        // 3. region의 변화가 감지될 시
        tv.append("region 변화감지~ \n");
    }

    @Override
    public void didExitRegion(RECOBeaconRegion recoBeaconRegion) {
        // 4. region 퇴장시
        tv.append("region 퇴장! \n");
    }

    @Override
    public void monitoringDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {
        // monitoring 실패 시
        tv.append("monitoring 실패... ㅠ.ㅠ \n");
    }

    /**
     * ---------------------------- 2. Ranging callbacks ----------------------------------------
     */

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> collection, RECOBeaconRegion recoBeaconRegion) {
        // ranging중인 region에서, 1초 간격으로 변경사항을 받아 이 callback을 부름

        tv.append("ranging..! - ");

        if(collection.size() == 0){
            tv.append(recoBeaconRegion.getUniqueIdentifier() + " 안잡힘");
        } else
        {
            if (recoBeaconRegion.getUniqueIdentifier().equals("1번 비콘") || recoBeaconRegion.getUniqueIdentifier().equals("2번 비콘")
                    ||recoBeaconRegion.getUniqueIdentifier().equals("3번 비콘")) {
                TextView proxText = (TextView) findViewById(R.id.textView);


                ArrayList<RECOBeacon> mRangedBeacons = new ArrayList<RECOBeacon>(collection);

                for (RECOBeacon beacon : mRangedBeacons) {
                    proxText.append("떨어진 정도 : " + beacon.getProximity() + "\n");
                    proxText.append("" + beacon.getAccuracy() + " meters \n" );
                }
            }

        }

    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {
        // ranging 실패시
        tv.append("ranging 실패... ㅠ.ㅠ \n");
    }

    /**
     * ------------------------------------------------------------------------------------------
     */


}