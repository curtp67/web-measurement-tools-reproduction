/*
 * This file is part of the Deterministic Network Calculator (DNC).
 *
 * Copyright (C) 2011 - 2018 Steffen Bondorf
 * Copyright (C) 2017 - 2018 The DiscoDNC contributors
 * Copyright (C) 2019+ The DNC contributors
 *
 * http://networkcalculus.org
 *
 *
 * The Deterministic Network Calculator (DNC) is free software;
 * you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package org.networkcalculus.dnc.demos;

import org.networkcalculus.dnc.CompFFApresets;
import org.networkcalculus.dnc.curves.ArrivalCurve;
import org.networkcalculus.dnc.curves.Curve;
import org.networkcalculus.dnc.curves.ServiceCurve;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.ServerGraph;
import org.networkcalculus.dnc.tandem.analyses.TotalFlowAnalysis;
import org.networkcalculus.dnc.AnalysisConfig.Multiplexing;

public class Polack_Tandem {

    public Polack_Tandem() {
    }

    public static void main(String[] args) {
        Polack_Tandem demo = new Polack_Tandem();

        try {
            demo.run();
        } catch (Exception e) {
        		e.printStackTrace();
        }
    }

public void run() throws Exception {
    ServiceCurve service_curve_1 = Curve.getFactory()
    .createRateLatency(5.0, 0);
    ServiceCurve service_curve_2 = Curve.getFactory()
    .createRateLatency(4.9, 0);
    ServiceCurve service_curve_3 = Curve.getFactory()
    .createRateLatency(4.5, 0);

    ServerGraph sg = new ServerGraph();
    
    Server s0 = sg.addServer(service_curve_1, Multiplexing.FIFO);
    Server s1 = sg.addServer(service_curve_2, Multiplexing.FIFO);
    Server s2 = sg.addServer(service_curve_3, Multiplexing.FIFO);
    
    sg.addTurn(s0, s1);
    sg.addTurn(s1, s2);
    
    ArrivalCurve arrival_curve = Curve.getFactory().createTokenBucket(15, 0);
    sg.addFlow("f0", arrival_curve, s0, s2);
    CompFFApresets compffa_analyses = new CompFFApresets( sg );
    TotalFlowAnalysis tfa = compffa_analyses.tf_analysis;
    
    tfa.performAnalysis(sg.getFlow(0));
    System.out.println("delay bound: " + tfa.getDelayBound());
    System.out.println("backlog bound: " + tfa.getBacklogBound());
}
}