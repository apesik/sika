/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.unice.vicc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

/**
 *
 * @author Malou
 */
public class MyNoViolationsPolicy extends VmAllocationPolicy {

    private Map<String, Host> vmTable;

    public MyNoViolationsPolicy(List<? extends Host> list) {
        super(list);
        vmTable = new HashMap<>();

    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        List<Host> hostList = getHostList();
        int i=0;
       Collections.sort(hostList, new HostMipsComparator());

        for (Host host : hostList) {
            if (vm.getMips() < host.getAvailableMips()) {
                    if (host.vmCreate(vm)) {
                        vmTable.put(vm.getUid(), host);
                        return true;
                    }
            }
        }

        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        if (host.vmCreate(vm)) {
            vmTable.put(vm.getUid(), host);
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
        return null;
    }

    @Override
    public void deallocateHostForVm(Vm vm) {
        vmTable.get(vm.getUid()).vmDestroy(vm);
    }

    @Override
    public Host getHost(Vm vm) {
        return vmTable.get(vm.getUid());
    }

    @Override
    public Host getHost(int i, int i1) {
        return vmTable.get(Vm.getUid(i1, i));
    }
    
     public class HostMipsComparator implements Comparator<Host> {

        @Override
        public int compare(Host o1, Host o2) {
            Double d1=new Double(o1.getAvailableMips());
            Double d2=new Double(o2.getAvailableMips());
            return d2.compareTo(d1);
        }

    }

}
