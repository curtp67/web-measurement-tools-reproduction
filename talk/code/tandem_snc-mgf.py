"""Small examples to play with."""
# TODO: Add .to_name-method to classes

from typing import List

from h_mitigator.fat_cross_perform import FatCrossPerform
from h_mitigator.optimize_mitigator import OptimizeMitigator
from nc_arrivals.arrival_distribution import ArrivalDistribution
from nc_arrivals.markov_modulated import MMOOFluid
from nc_arrivals.qt import DM1
from nc_operations.perform_enum import PerformEnum
from nc_server.constant_rate_server import ConstantRateServer
from optimization.optimize import Optimize
from optimization.sim_anneal_param import SimAnnealParams
from utils.perform_parameter import PerformParameter
from h_mitigator.single_server_mit_perform import SingleServerMitPerform
from nc_operations.operations  import Convolve

if __name__ == '__main__':
    print("Tandem Performance Bounds:\n")

    DELAY_PROB_BOUND = PerformParameter(perform_metric=PerformEnum.DELAY_PROB,
    value=.05)

    Server1 = ConstantRateServer(rate=5.0)
    Server2 = ConstantRateServer(rate=4.9)
    Server3 = ConstantRateServer(rate=4.5)

    ConvolvedServer = Convolve(Convolve(Server1,Server2),Server3)

    TandemTopology = SingleServerMitPerform(arr_list=[DM1(lamb=.3)],
                                    server=ConvolvedServer,
                                    perform_param=DELAY_PROB_BOUND)
                                    
    #Grid search for param between 0.1 and 5.0 with granularity 0.1
    print(Optimize(TandemTopology, number_param=1,
                   print_x=True).grid_search(bound_list=[(0.1, 5.0)], 
                   delta=0.1))
