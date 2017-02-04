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
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends Activity implements RECOServiceConnectListener, RECOMonitoringListener {

    public static final String RECO_UUID = "24DDF411-8CF1-440C-87CD-E368DAF9C93E";

    private TextView tv;

    boolean mScanRecoOnly = true;
    boolean mEnableBackgroundTimeout = true;

    RECOBeaconManager recoManager;
    ArrayList<RECOBeaconRegion> monitoringRegions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Beacon시작");

        recoManager = RECOBeaconManager.getInstance(this, mScanRecoOnly, mEnableBackgroundTimeout);

        recoManager.setMonitoringListener(this);

        //scan 시간을 설정할 수 있습니다. 기본 값은 1초 입니다.
        recoManager.setScanPeriod(1000);

        //scan 후, 다음 scan 시작 전까지의 시간을 설정할 수 있습니다. 기본 값은 10초 입니다.
        recoManager.setSleepPeriod(10000);

        recoManager.bind(this);
        ///////////////////// 여기까지 되면 onServiceConnected() 불림 ////////////////////////////

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            recoManager.unbind();
        } catch (RemoteException re)
        {
            re.printStackTrace();
        }

        for(RECOBeaconRegion region : monitoringRegions) {
            try {
                recoManager.stopMonitoringForRegion(region);
            } catch (RemoteException e) {
                //RemoteException 발생 시 작성 코드
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
            }
        }
    }

    /////////////////////////////// service 연결 부분 /////////////////////////////////////////
    //연결이 되었을때 제공되는 서비스 부분
    @Override
    public void onServiceConnect() {
        tv.setText(tv.getText() + "\n연결되었습니다");


        // 이걸 ServiceConnect()안에서 수행하는걸로 하면 된다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        monitoringRegions = new ArrayList<RECOBeaconRegion>();
        monitoringRegions.add(new RECOBeaconRegion(RECO_UUID, 501, "울산"));


        for(RECOBeaconRegion region : monitoringRegions) {
            try {

                //region의 expiration 시간을 설정할 수 있습니다. 기본 값은 60초(1분) 입니다.
                region.setRegionExpirationTimeMillis(60000);
                recoManager.startMonitoringForRegion(region);

            } catch (RemoteException e) {
                //RemoteException 발생 시 작성 코드
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
            }
        }


    }

    @Override
    public void onServiceFail(RECOErrorCode arg0) {
        // TODO Auto-generated method stub
        tv.setText(tv.getText() + "\nService failed.");
    }


    ////////////////////////////// monitoring 관련 //////////////////////////////////////////

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



}
