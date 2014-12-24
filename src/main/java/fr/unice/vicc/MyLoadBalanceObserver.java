package fr.unice.vicc;

import static fr.unice.vicc.MyAntiAffinityObserver.DEFAULT_DELAY;
import static fr.unice.vicc.MyAntiAffinityObserver.OBSERVE;
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
public class MyLoadBalanceObserver extends SimEntity {

    public static final int OBSERVE = 728777;
    private List<PowerHost> hosts;
    private float delay;
    public static final float DEFAULT_DELAY = 1;
    double utilisationRamTotal = 0.;
    double utilisationCPUTotal = 0.;
    double utilisationCPUMIPSTotal = 0.;
    int utilisationRamCount = 0;
    int utilisationCPUCount = 0;
    int utilisationCPUMIPSCount = 0;

    public MyLoadBalanceObserver(List<PowerHost> hosts) {
        this(hosts, DEFAULT_DELAY);
    }

    public MyLoadBalanceObserver(List<PowerHost> hosts, float delay) {
        super("MyLoadBalanceObserver");
        this.hosts = hosts;
        this.delay = delay;
    }

    @Override
    public void processEvent(SimEvent ev) {
        switch (ev.getTag()) {

            case OBSERVE:
                double utilisationRam = 0.;
                double utilisationCPU = 0.;
                double utilisationCPUMIPS = 0.;

                Map<Integer, List<Vm>> m = new HashMap<>();
                for (PowerHost ph : hosts) {
                    utilisationRam += ph.getUtilizationOfRam();
                    utilisationCPU += ph.getUtilizationOfCpu();
                    utilisationCPUMIPS += ph.getAvailableMips();
                }
                utilisationRamTotal += utilisationRam / hosts.size();
                utilisationCPUTotal += utilisationCPU / hosts.size();
                utilisationCPUMIPSTotal += utilisationCPUMIPS / hosts.size();
                utilisationCPUCount++;

                // Log.printLine(getName()+" Ratio RAM: ");                    
                // Log.printLine(getName()+" Ratio CPU : "+utilisationCPU/hosts.size());                    
                //  Log.printLine(getName()+" Ratio CPU MIPS: "+utilisationCPUMIPS/hosts.size());                    

                send(this.getId(), delay, OBSERVE, null);


        }
    }

    @Override
    public void shutdownEntity() {
        Log.printLine(getName() + " Ratio RAM: "+utilisationRamTotal/utilisationCPUCount);
        Log.printLine(getName() + " Ratio CPU : " + utilisationCPUTotal /utilisationCPUCount);
        Log.printLine(getName() + " Ratio CPU MIPS: " + utilisationCPUMIPSTotal /utilisationCPUCount);

        Log.printLine(getName() + " is shutting down...");

    }

    @Override
    public void startEntity() {
        Log.printLine(getName() + " is starting...");
        send(this.getId(), delay, OBSERVE, null);
    }
}
