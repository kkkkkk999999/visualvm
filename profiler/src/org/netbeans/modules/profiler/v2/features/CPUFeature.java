/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.netbeans.modules.profiler.v2.features;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import org.netbeans.lib.profiler.TargetAppRunner;
import org.netbeans.lib.profiler.common.Profiler;
import org.netbeans.lib.profiler.common.ProfilingSettings;
import org.netbeans.lib.profiler.common.ProfilingSettingsPresets;
import org.netbeans.lib.profiler.results.cpu.FlatProfileContainer;
import org.netbeans.lib.profiler.results.cpu.FlatProfileProvider;
import org.netbeans.lib.profiler.ui.UIUtils;
import org.netbeans.lib.profiler.ui.components.ProfilerToolbar;
import org.netbeans.lib.profiler.ui.cpu.CPUTableView;
import org.netbeans.modules.profiler.api.icons.GeneralIcons;
import org.netbeans.modules.profiler.api.icons.Icons;
import org.netbeans.modules.profiler.api.icons.ProfilerIcons;
import org.netbeans.modules.profiler.v2.session.ProjectSession;
import org.netbeans.modules.profiler.v2.ui.components.PopupButton;
import org.netbeans.modules.profiler.v2.ui.components.SmallButton;
import org.netbeans.modules.profiler.v2.ui.components.TitledMenuSeparator;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Jiri Sedlacek
 */
@NbBundle.Messages({
    "CPUFeature_name=CPU",
    "CPUFeature_lrLabel=Live results:",
    "CPUFeature_viewHotSpots=Hot Spots",
    "CPUFeature_viewCallTree=Call Tree",
    "CPUFeature_viewCombined=Combined",
    "CPUFeature_pdLabel=Profiling data:", 
    "CPUFeature_snapshot=Snapshot", 
    "CPUFeature_apLabel=Application:", 
    "CPUFeature_threadDump=Thread Dump"
})
final class CPUFeature extends ProfilerFeature.Basic {
    
    private static enum View { HOT_SPOTS, CALL_TREE, COMBINED }
    
    private JLabel lrLabel;
    private JButton lrPauseButton;
    private JToggleButton lrRefreshButton;
    private PopupButton lrView;
    
    private JLabel pdLabel;
    private JButton pdSnapshotButton;
    private JButton pdResetResultsButton;
    
    private JLabel apLabel;
    private JButton apThreadDumpButton;
    
    private ProfilerToolbar toolbar;
    private JPanel settingsUI;
    
    private View view;
    
    private CPUTableView tableView;
    private ProfilingSettings settings;
    
    
    CPUFeature() {
        super(Bundle.CPUFeature_name(), Icons.getIcon(ProfilerIcons.CPU));
    }

    
    public JPanel getResultsUI() {
        if (tableView == null) initResultsUI();
        return tableView;
    }
    
    public JPanel getSettingsUI() {
        if (settingsUI == null) {
            settingsUI = new JPanel();
            settingsUI.setOpaque(false);
            settingsUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            settingsUI.setLayout(new BoxLayout(settingsUI, BoxLayout.LINE_AXIS));

    //        panel.add(Box.createHorizontalStrut(10));

            settingsUI.add(new JLabel("Profile:"));

            settingsUI.add(Box.createHorizontalStrut(5));

            settingsUI.add(new PopupButton("All classes") {
                protected void populatePopup(JPopupMenu popup) {
                    popup.add(new TitledMenuSeparator("Quick (sampled)"));
                    popup.add(new JRadioButtonMenuItem("All classes", true));
                    popup.add(new JMenuItem("Project classes"));
                    
                    popup.add(new TitledMenuSeparator("Advanced (instrumented)"));
                    popup.add(new JMenuItem("Single class"));
                    popup.add(new JMenuItem("Single method"));
                }
            });

            settingsUI.add(Box.createHorizontalStrut(5));


            settingsUI.add(Box.createGlue());


            settingsUI.add(new JLabel("Overhead:"));

            settingsUI.add(Box.createHorizontalStrut(5));

            settingsUI.add(new JProgressBar() {
                public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    d.width = 80;
                    return d;
                }
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }
            });

            settingsUI.add(Box.createHorizontalStrut(5));

            Component sep1 = Box.createHorizontalStrut(1);
            sep1.setBackground(Color.GRAY);
            if (sep1 instanceof JComponent) ((JComponent)sep1).setOpaque(true);
            Dimension d = sep1.getMaximumSize();
            d.height = 20;
            sep1.setMaximumSize(d);
            settingsUI.add(sep1);

            settingsUI.add(Box.createHorizontalStrut(5));

            settingsUI.add(new SmallButton("Apply"));

            settingsUI.add(Box.createHorizontalStrut(5));

            settingsUI.add(new SmallButton("Cancel"));
        }
        return settingsUI;
    }
    
    public ProfilerToolbar getToolbar() {
        if (toolbar == null) {
            lrLabel = new JLabel(Bundle.CPUFeature_lrLabel());
            lrLabel.setForeground(UIUtils.getDisabledLineColor());
            
            lrPauseButton = new JButton(Icons.getIcon(GeneralIcons.PAUSE));
            lrPauseButton.setEnabled(false);
            
            lrRefreshButton = new JToggleButton(Icons.getIcon(GeneralIcons.UPDATE_NOW));
            lrRefreshButton.setEnabled(false);
            
            lrView = new PopupButton() {
                protected void populatePopup(JPopupMenu popup) { populateViews(popup); }
            };
            lrView.setEnabled(false);
            
            pdLabel = new JLabel(Bundle.CPUFeature_pdLabel());
            pdLabel.setForeground(UIUtils.getDisabledLineColor());
            
            pdSnapshotButton = new JButton(Bundle.CPUFeature_snapshot(), Icons.getIcon(ProfilerIcons.SNAPSHOT_TAKE));
            pdSnapshotButton.setEnabled(false);
            
            pdResetResultsButton = new JButton(Icons.getIcon(ProfilerIcons.RESET_RESULTS));
            pdResetResultsButton.setEnabled(false);
            
            apLabel = new JLabel(Bundle.CPUFeature_apLabel());
            apLabel.setForeground(UIUtils.getDisabledLineColor());
            
            apThreadDumpButton = new JButton(Bundle.CPUFeature_threadDump(), Icons.getIcon(ProfilerIcons.WINDOW_THREADS));
            apThreadDumpButton.setEnabled(false);
            
            toolbar = ProfilerToolbar.create(true);
            
            toolbar.addSpace(2);
            toolbar.addSeparator();
            toolbar.addSpace(5);
            
            toolbar.add(lrLabel);
            toolbar.addSpace(2);
            toolbar.add(lrPauseButton);
            toolbar.add(lrRefreshButton);
            toolbar.add(lrView);
            
            toolbar.addSpace(2);
            toolbar.addSeparator();
            toolbar.addSpace(5);
            
            toolbar.add(pdLabel);
            toolbar.addSpace(2);
            toolbar.add(pdSnapshotButton);
            toolbar.add(pdResetResultsButton);
            
            toolbar.addSpace(2);
            toolbar.addSeparator();
            toolbar.addSpace(5);
            
            toolbar.add(apLabel);
            toolbar.addSpace(2);
            toolbar.add(apThreadDumpButton);
            
            toolbar.addFiller();
            
            setView(View.HOT_SPOTS);
        }
        
        return toolbar;
    }
    
    public ProfilingSettings getSettings() {
        if (settings == null) {
            settings = ProfilingSettingsPresets.createCPUPreset();
        }
        return settings;
    }
    
    private void populateViews(JPopupMenu popup) {
        popup.add(new JRadioButtonMenuItem(Bundle.CPUFeature_viewHotSpots(), getView() == View.HOT_SPOTS) {
            protected void fireActionPerformed(ActionEvent e) { setView(View.HOT_SPOTS); }
        });
        
        popup.add(new JRadioButtonMenuItem(Bundle.CPUFeature_viewCallTree(), getView() == View.CALL_TREE) {
            protected void fireActionPerformed(ActionEvent e) { setView(View.CALL_TREE); }
        });
        
        popup.add(new JRadioButtonMenuItem(Bundle.CPUFeature_viewCombined(), getView() == View.COMBINED) {
            protected void fireActionPerformed(ActionEvent e) { setView(View.COMBINED); }
        });
    }

    private void setView(View view) {
        if (view == this.view) return;
        
        this.view = view;
        
        switch (view) {
            case HOT_SPOTS:
                lrView.setText(Bundle.CPUFeature_viewHotSpots());
                break;
            case CALL_TREE:
                lrView.setText(Bundle.CPUFeature_viewCallTree());
                break;
            case COMBINED:
                lrView.setText(Bundle.CPUFeature_viewCombined());
                break;
        }
    }
    
    private View getView() {
        return view;
    }
    
    private void initResultsUI() {
        tableView = new CPUTableView();
    }
    
    public void stateChanged(ProjectSession.State oldState, ProjectSession.State newState) {
        if (newState == null || newState == ProjectSession.State.INACTIVE) {
            stopResults();
        } else {
            startResults();
        }
    }
    
    private RequestProcessor processor;
    
    private void startResults() {
        if (tableView != null) tableView.setData(null);
        
        if (processor == null) processor = new RequestProcessor("CPU Data Refresher"); // NOI18N
        
        Runnable refresher = new Runnable() {
            public void run() {
                if (tableView != null) {
                    TargetAppRunner runner = Profiler.getDefault().getTargetAppRunner();
                    FlatProfileProvider dataProvider = runner.getProfilerClient().getFlatProfileProvider();
                    FlatProfileContainer data = dataProvider == null ? null : dataProvider.createFlatProfile();
                    if (data != null) tableView.setData(data);
                }
                
                if (processor != null && !processor.isShutdown()) processor.post(this, 1500);
            }
        };
        
        processor.post(refresher, 500);
    }
    
    private void stopResults() {
        if (processor != null) processor.shutdownNow();
        processor = null;
    }
    
//    public void attachedToSession(ProjectSession session) {
//        super.attachedToSession(session);
//        if (tableView != null) tableView.setData(null);
//    }
    
//    public void detachedFromSession(ProjectSession session) {
//        super.detachedFromSession(session);
//        if (tableView != null) tableView.setData(null);
//    }
    
}
