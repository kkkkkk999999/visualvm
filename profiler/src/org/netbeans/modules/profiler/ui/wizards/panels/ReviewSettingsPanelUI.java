/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
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

package org.netbeans.modules.profiler.ui.wizards.panels;

import java.text.MessageFormat;
import org.openide.util.NbBundle;

/**
 *
 * @author  Jaroslav Bachorik
 */
public class ReviewSettingsPanelUI extends javax.swing.JPanel {
  private ReviewSettingsPanel.Model model;
  private final String REVIEW_HINT = NbBundle.getMessage(this.getClass(), "AttachSettingsSummaryWizardPanelUI_FinishContinueMsg"); // NOI18N

  /** Creates new form ReviewSettingsPanelUI */
  public ReviewSettingsPanelUI(ReviewSettingsPanel.Model model) {
    this.model = model;
    initComponents();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        borderPanel = new javax.swing.JPanel();
        reviewScroll = new javax.swing.JScrollPane();
        textReview = new org.netbeans.lib.profiler.ui.components.HTMLTextArea();
        textReview.setDisabledTextColor(javax.swing.UIManager.getColor("Label.foreground"));
        hintPanel = new org.netbeans.modules.profiler.ui.wizards.panels.components.ResizableHintPanel();

        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(400, 300));
        setPreferredSize(new java.awt.Dimension(500, 400));

        label.setLabelFor(textReview);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/netbeans/modules/profiler/ui/wizards/panels/Bundle"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(label, bundle.getString("AttachSettingsSummaryWizardPanelUI_AttachSettingsOverviewString")); // NOI18N

        borderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        borderPanel.setLayout(new javax.swing.BoxLayout(borderPanel, javax.swing.BoxLayout.LINE_AXIS));

        textReview.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        textReview.setText(getSummary());
        reviewScroll.setViewportView(textReview);

        borderPanel.add(reviewScroll);

        hintPanel.setMaximumSize(new java.awt.Dimension(500, 100));
        hintPanel.setMinimumSize(new java.awt.Dimension(500, 40));
        hintPanel.setPreferredSize(new java.awt.Dimension(500, 80));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(hintPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(borderPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .add(label))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(label)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(borderPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hintPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        hintPanel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ReviewSettingsPanelUI.class, "ReviewSettingsPanelUI.hintPanel.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents
  
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel borderPanel;
    private org.netbeans.modules.profiler.ui.wizards.panels.components.ResizableHintPanel hintPanel;
    private javax.swing.JLabel label;
    private javax.swing.JScrollPane reviewScroll;
    private org.netbeans.lib.profiler.ui.components.HTMLTextArea textReview;
    // End of variables declaration//GEN-END:variables
  
  public String getSummary() {
    return this.model.getSummary();
  }
  
  public void refresh() {
    textReview.setText(this.model.getSummary());
    hintPanel.setHint(MessageFormat.format(REVIEW_HINT, new Object[]{model.getProviderName()}));
  }
}
