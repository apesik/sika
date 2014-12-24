package fr.unice.vicc;

import java.util.ArrayList;
import java.util.HashMap;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.Vm;

/**
 *
 *
 * @author malou
 */
public class MyAntiAffinityObserver extends SimEntity {

    public static final int OBSERVE = 728079;
    private List<PowerHost> hosts;
    private float delay;
    public static final float DEFAULT_DELAY = 1;

    public MyAntiAffinityObserver(List<PowerHost> hosts) {
        this(hosts, DEFAULT_DELAY);
    }

    public MyAntiAffinityObserver(List<PowerHost> hosts, float delay) {
        super("MyAntiAffinityObserver");
        this.hosts = hosts;
        this.delay = delay;
    }


    @Override
    public void processEvent(SimEvent ev) {
        switch (ev.getTag()) {

            case OBSERVE: 
                Map<Integer, List<Vm>> m = new HashMap<>();
                for (PowerHost ph : hosts) {
                    
                    int i = 0;
                    m.put(ph.getId(), null);
                    for (Vm vm : ph.getVmList()) {
                        if (i == 0) {
                            List l = new ArrayList();
                            l.add(vm);
                            m.put(ph.getId(), l);
                        } else {
                            
                            if (
                               (vm.getId()/Constants.AFFINITY_ID_BY_NODE_NUMBERS
                                    == m.get(ph.getId()).get(0).getId()/Constants.AFFINITY_ID_BY_NODE_NUMBERS)) {
                                List<Vm> get = m.get(ph.getId());
                                get.add(vm);
                                m.put(ph.getId(), get);
                            }
                        }
                        i++;
                    }

                }
                String str = "";
                for (PowerHost ph : hosts) {
                    if (m.get(ph.getId())!=null) {
                        if (m.get(ph.getId()).size() > 1) {
                            str += getName() + ": Vms (";
                            for (Vm v : m.get(ph.getId())) {
                                str += v.getId() + " ";
                            }
                            str += ") must not been on same node #"+ph.getId()+"\n";
                        }
                    }
                }
                if(!str.isEmpty()){
                  Log.printLine(str);                    
                }

                send(this.getId(), delay, OBSERVE, null);


        }
    }


    @Override
    public void shutdownEntity() {
        Log.printLine(getName() + " is shutting down...");
    }

    @Override
    public void startEntity() {
        Log.printLine(getName() + " is starting...");
        send(this.getId(), delay, OBSERVE, null);
    }
}
