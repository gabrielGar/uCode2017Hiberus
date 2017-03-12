package com.ubeacon.rts.ubeacon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ubeacon.rts.R;

import ibeacon.IBeacon;
import ibeacon.IBeaconConsumer;
import ibeacon.IBeaconManager;
import ibeacon.MonitorNotifier;
import ibeacon.RangeNotifier;
import ibeacon.Region;

public class MonitoringActivity extends Activity implements IBeaconConsumer {
    protected static final String TAG = "MonitoringActivity";
    Context cntx;
    private ListView list = null;
    private BeaconAdapter adapter = null;
    private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();
    private LayoutInflater inflater;

    private BeaconServiceUtility beaconUtill = null;
    private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
    private HashMap<String, BeaconDataHotel> beaconCacheList = new HashMap<String, BeaconDataHotel>();
    private ProgressBar progressbar;
    private ArrayList<Integer> beaconsListed = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        cntx = this.getApplication();
        beaconUtill = new BeaconServiceUtility(this);
        list = (ListView) findViewById(R.id.list);
        adapter = new BeaconAdapter();
        list.setAdapter(adapter);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intt = new Intent(getApplicationContext(), WebViewInfo.class);
                RelativeLayout rl = (RelativeLayout) view;
                intt.putExtra("html", (beaconCacheList.get(arrayL.get(beaconsListed.get(position)).getProximityUuid())).htmlContent);
                startActivity(intt);
            }
        });
        progressbar = (ProgressBar) findViewById(R.id.progressBar2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconUtill.onStart(iBeaconManager, this);
    }

    @Override
    protected void onStop() {
        beaconUtill.onStop(iBeaconManager, this);
        super.onStop();
    }

    @Override
    public void onIBeaconServiceConnect() {

        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {

                arrayL.clear();
                beaconsListed.clear();
                arrayL.addAll((ArrayList<IBeacon>) iBeacons);
                for (int i = 0; i < arrayL.size(); i++){
                    if(beaconCacheList.containsKey(arrayL.get(i).getProximityUuid()) && arrayL.get(i).getProximity() <= IBeacon.PROXIMITY_NEAR){
                        beaconsListed.add(i);
                    }
                }
                adapter.notifyDataSetChanged();
                //Optimizar
                if(beaconsListed.size() > 0){
                    progressbar.setVisibility(View.GONE);
                }else{
                    progressbar.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < iBeacons.size(); i++) {
                    IBeacon aux = ((ArrayList<IBeacon>) iBeacons).get(i);
                    BeaconDataHotel bdh = null;
                    if(beaconCacheList.get(aux.getProximityUuid()) == null) {
                        try {
                            int id = getResources().getIdentifier(aux.getProximityUuid().replace("-", ""), "raw", getPackageName());
                            InputStream is = getResources().openRawResource(id);
                            bdh = new BeaconDataHotel(is);
                            beaconCacheList.put(aux.getProximityUuid(), bdh);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (aux.getProximity() <= IBeacon.PROXIMITY_NEAR) {
                        //Lanzar notificacion por cercania
                        if (bdh != null && !bdh.notificado) {
                            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.piedraprueba)
                                    .setContentTitle(bdh.beaconName)
                                    .setAutoCancel(true)
                                    .setContentText(bdh.description);
                            Intent intt = new Intent(getApplicationContext(), WebViewInfo.class);
                            intt.putExtra("html", (beaconCacheList.get(aux.getProximityUuid())).htmlContent);
                            PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intt, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(resultPendingIntent);
                            int mNotificationId = i;
                            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());
                            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            v.vibrate(500);
                            bdh.notificado = true;
                        }
                    }
                }
            }

        });

        iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.e("BeaconDetactorService", "didEnterRegion");

                // logStatus("I just saw an iBeacon for the first time!");

            }

            @Override
            public void didExitRegion(Region region) {
                Log.e("BeaconDetactorService", "didExitRegion");
                // logStatus("I no longer see an iBeacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.e("BeaconDetactorService", "didDetermineStateForRegion");
                // logStatus("I have just switched from seeing/not seeing iBeacons: " + state);
            }

        });

        try {
            iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class BeaconAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (beaconsListed != null && beaconsListed.size() > 0){
                return beaconsListed.size();

            }
            else
                return 0;
        }

        @Override
        public IBeacon getItem(int arg0) {
            return arrayL.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                ViewHolder holder;

                if (convertView != null) {
                    holder = (ViewHolder) convertView.getTag();
                } else {
                    holder = new ViewHolder(convertView = inflater.inflate(R.layout.tupple_monitoring, null));
                }
                IBeacon myBeacon = arrayL.get(beaconsListed.get(position));
                BeaconDataHotel bdh = beaconCacheList.get(myBeacon.getProximityUuid());
                if (myBeacon.getProximityUuid() != null && myBeacon.getProximity() <= IBeacon.PROXIMITY_NEAR) {
                    holder.beacon_description.setText(bdh.description);
                    holder.beacon_name.setText(bdh.beaconName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        private class ViewHolder {
            private TextView beacon_name;
            private TextView beacon_description;
            private RelativeLayout beacon_layout;

            public ViewHolder(View view) {
                beacon_description = (TextView) view.findViewById(R.id.BEACON_description);
                beacon_name = (TextView) view.findViewById(R.id.BEACON_name);
                beacon_layout = (RelativeLayout) view.findViewById(R.id.lanzaNotificacion);
                view.setTag(this);
            }
        }

    }

}