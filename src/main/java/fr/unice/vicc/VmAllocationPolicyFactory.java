package fr.unice.vicc;

import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.List;

/**
 * @author Fabien Hermenier
 */
public class VmAllocationPolicyFactory {

    VmAllocationPolicy make(String id, List<PowerHost> hosts) {
        switch (id) {
            case "naive":  return new MyNaivePolicy(hosts);
            case "antiAffinity": return new MyAntiAffinityPolicy(hosts);
            case "balance": return new MyLoadBalancePolicy(hosts);
            case "noViolations": return new MyNoViolationsPolicy(hosts);
        }
        throw new IllegalArgumentException("No such policy '" + id + "'");
    }
}
