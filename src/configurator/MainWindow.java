/*
 * Developer: Jack Loss
 * File: MainWindow.java
 * Date: October 2, 2017
 * Purpose: Creates configurator GUI, handles events, performs file I/O to CSV and PDF formats
 */
package configurator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.ImageIcon;

/**
 * @author Jack
 */
public class MainWindow extends javax.swing.JFrame {

    Cayman cayman = new Cayman();
    
    URL faviconURL = getClass().getResource("images/ui-items/icons/porsche-favicon.png");
    ImageIcon favicon = new ImageIcon(faviconURL);
    
    private static final DecimalFormat DF = new DecimalFormat("$#,###");
    private static final NumberFormat NF = NumberFormat.getCurrencyInstance();
    private static final String USERHOMEFOLDER = System.getProperty("user.home");
    private static final File CSVFILE = new File(USERHOMEFOLDER, "cayman-spec.csv");
    private static final File PDFFILE = new File(USERHOMEFOLDER, "cayman-spec.pdf");
        
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        this.setIconImage(favicon.getImage());
        getContentPane().setBackground(Color.WHITE);
        this.setLocationRelativeTo(null);
    }

    public void checkSelectedOptions() {
        cayman.getOptionNames().setLength(0);
        cayman.getOptionPrices().setLength(0);
        if(mainSeatHeatCheckBox.isSelected()) {
            cayman.addOption("Seat Heating", 530);
        } if(mainPASMCheckBox.isSelected()) {
            cayman.addOption("Porsche Active Stability Management (PASM)", 1790);
        } if(mainBoseCheckBox.isSelected()) {
            cayman.addOption("BOSE® Surround Sound System", 990);
        } if(mainNavCheckBox.isSelected()) {
            cayman.addOption("Navigation Module", 1780);
        }
    }
    
    public int getEquipPrice() {
        int price = 0;
        if(mainSeatHeatCheckBox.isSelected()) {
            price += 530;
        } if(mainPASMCheckBox.isSelected()) {
            price += 1790;
        } if(mainBoseCheckBox.isSelected()) {
            price += 990;
        } if(mainNavCheckBox.isSelected()) {
            price += 1780;
        }
        return price;
    }
    
    //Change all components' background color to white in a given container
    @SuppressWarnings("empty-statement")
    private void getComponents(Container container) {

    Component[] components = container.getComponents();
        for (Component component : components) {
            if ("javax.swing.JPanel".equals(component.getClass().getName())) {
                component.setBackground(Color.WHITE);
            } if (container.getClass().isInstance(component)) {
                ;
            }
            getComponents((Container) component);
        }
    }
    
    public void setPriceFields() {
        int basePrice = 67700;
        int equipPrice = getEquipPrice();
        equipPrice += cayman.getExtColorPrice()
                    + cayman.getWheelStylePrice()
                    + cayman.getIntColorPrice()
                    + cayman.getTransPrice();
        int deliverPrice = 1050;
        int totalPrice = basePrice + equipPrice + deliverPrice;
        mainEquipPriceDollarLabel.setText(DF.format(equipPrice));
        mainTotalPriceDollarLabel.setText(DF.format(totalPrice));
        overviewEquipPriceDollarLabel.setText(DF.format(equipPrice));
        overviewTotalPriceDollarLabel.setText(DF.format(totalPrice));
    }
    
    //Determines current specification, builds path to corresponding model-image
    //directory, loads proper images to GUI model display
    public void setImagePath(String displayName) {
        StringBuilder path = new StringBuilder(50);
        
        String extColorPath = cayman.getExtColorPath();
        String wheelStylePath = cayman.getWheelStylePath();
        String intColorPath = cayman.getIntColorPath();
        String transPath = cayman.getTransPath();
        String extension = "";
        if(displayName.equalsIgnoreCase("main window")) {
            if(mainImageSelect1Radio.isSelected()) {
                extension = "ext-1.jpg";
            } else if(mainImageSelect2Radio.isSelected()) {
                extension = "ext-2.jpg";
            } else if(mainImageSelect3Radio.isSelected()) {
                extension = "ext-3.jpg";
            } else if(mainImageSelect4Radio.isSelected()) {
                extension = "ext-4.jpg";
            } else if(mainImageSelect5Radio.isSelected()) {
                extension = "int-1.jpg";
            } else if(mainImageSelect6Radio.isSelected()) {
                extension = "int-2.jpg";
            }
        } else if(displayName.equalsIgnoreCase("overview window")) {
            if(overviewImageSelect1Radio.isSelected()) {
                extension = "ext-1.jpg";
            } else if(overviewImageSelect2Radio.isSelected()) {
                extension = "ext-2.jpg";
            } else if(overviewImageSelect3Radio.isSelected()) {
                extension = "ext-3.jpg";
            } else if(overviewImageSelect4Radio.isSelected()) {
                extension = "ext-4.jpg";
            } else if(overviewImageSelect5Radio.isSelected()) {
                extension = "int-1.jpg";
            } else if(overviewImageSelect6Radio.isSelected()) {
                extension = "int-2.jpg";
            }
        }
                
        path.append(extColorPath);
        path.append("/");
        path.append(wheelStylePath);
        path.append("/");
        path.append(intColorPath);
        path.append("/");
        path.append(transPath);
        path.append("/");
        path.append(extension);
                
        String finalPath = path.toString();
        
        Image caymanDisplayImage = new ImageIcon(this.getClass().getResource("images/cayman-model/" + finalPath)).getImage();
        ImageIcon caymanDisplayIcon = new ImageIcon(caymanDisplayImage);
        if(displayName.equals("main window")) {
            mainCaymanDisplayLabel.setIcon(caymanDisplayIcon);
        } else if(displayName.equals("overview window")) {
            overviewCaymanDisplayLabel.setIcon(caymanDisplayIcon);    
        }
    }
    
    //Sets components of overview window to match specification from main window
    //at the time the "Show Overview" button is clicked
    public void initOverviewDialog() {
        setImagePath("overview window");
        getComponents(overviewDialog);
        overviewGrayDescriptionPanel.setBackground(Color.LIGHT_GRAY);
        overviewDialog.setIconImage(favicon.getImage());
        overviewDialog.setBackground(Color.WHITE);
        overviewPaintColorNameLabel.setText(cayman.getExtColorName());
        overviewPaintColorPriceLabel.setText(DF.format(cayman.getExtColorPrice()));
        overviewWheelStyleNameLabel.setText(cayman.getWheelStyleName());
        overviewWheelStylePriceLabel.setText(DF.format(cayman.getWheelStylePrice()));
        overviewLeatherColorNameLabel.setText(cayman.getIntColorName());
        overviewLeatherColorPriceLabel.setText(DF.format(cayman.getIntColorPrice()));
        overviewTransNameLabel.setText(cayman.getTransType());
        overviewTransPriceLabel.setText(DF.format(cayman.getTransPrice()));
        checkSelectedOptions();
        if(cayman.getOptionNames().length() == 0) {
            overviewAddUpgradesNameLabel.setText("N/A");
        } else {
            overviewAddUpgradesNameLabel.setText("<html>" + cayman.getOptionNames().toString() + "</html>");
        }
        if(cayman.getOptionPrices().length() == 0) {
            overviewAddUpgradesPriceLabel.setText("N/A");
        } else {
            overviewAddUpgradesPriceLabel.setText("<html><body style='text-align: right'>" + cayman.getOptionPrices().toString() + "</html>");
        }
        overviewDialog.setLocationRelativeTo(null);
        overviewDialog.setVisible(true);
    }
    
    public void printDialogToPDF() {
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDFFILE));
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template = contentByte.createTemplate(overviewDialog.getWidth(), overviewDialog.getHeight());
            Graphics2D g2 = template.createGraphics(overviewDialog.getWidth(), overviewDialog.getHeight());
            g2.scale(0.76, 0.76);
            g2.translate(0, 100);
            overviewDialog.print(g2);
            g2.dispose();
            contentByte.addTemplate(template, 0, 0);
            getComponents(successPDFDialog);
            successPDFDialog.setIconImage(favicon.getImage());
            successPDFDialog.setBackground(Color.WHITE);
            successPDFDialog.setLocationRelativeTo(null);
            successPDFDialog.setVisible(true);
        } catch (DocumentException | FileNotFoundException e) {
            getComponents(fileNotFoundPDFDialog);
            fileNotFoundPDFDialog.setIconImage(favicon.getImage());
            fileNotFoundPDFDialog.setBackground(Color.WHITE);
            fileNotFoundPDFDialog.setLocationRelativeTo(null);
            fileNotFoundPDFDialog.setVisible(true);
        } finally {
            if(document.isOpen()) {
                document.close();
            }
        }
    }
    
    public void printDialogToCSV() {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSVFILE);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("Item,Selection,Price");
            bufferedWriter.newLine();
            bufferedWriter.write("2018 Porsche 718 Cayman S,Base Price,67700");
            bufferedWriter.newLine();
            bufferedWriter.write("2018 Porsche 718 Cayman S,Price for Equipment," + NF.parse(overviewEquipPriceDollarLabel.getText()));
            bufferedWriter.newLine();
            bufferedWriter.write("2018 Porsche 718 Cayman S,Delivery/Processing/Handling Fee,1050");
            bufferedWriter.newLine();
            bufferedWriter.write("2018 Porsche 718 Cayman S,Total Price," + NF.parse(overviewTotalPriceDollarLabel.getText()));
            bufferedWriter.newLine();
            bufferedWriter.write("Paint Color," + cayman.getExtColorName() + "," + cayman.getExtColorPrice());
            bufferedWriter.newLine();
            bufferedWriter.write("Wheel Style," + cayman.getWheelStyleName() + "," + cayman.getWheelStylePrice());
            bufferedWriter.newLine();
            bufferedWriter.write("Interior Color," + cayman.getIntColorName() + "," + cayman.getIntColorPrice());
            bufferedWriter.newLine();
            bufferedWriter.write("Transmission Type," + cayman.getTransType() + "," + cayman.getTransPrice());
            bufferedWriter.newLine();
            
            String[] optionNamesString = cayman.getOptionNames().toString().split("<br>");
            String[] optionPricesString = cayman.getOptionPrices().toString().split("<br>");
            if(cayman.getOptionNames().length() == 0) {
                bufferedWriter.write("No Options Selected,N/A,0");
            } else {
                for(int i = 1; i <= optionNamesString.length; i++) {
                    bufferedWriter.write("Option " + i + "," + optionNamesString[i-1].replace("®", "") + "," + NF.parse(optionPricesString[i-1]));
                    bufferedWriter.newLine();
                }
            }
            getComponents(successCSVDialog);
            successCSVDialog.setIconImage(favicon.getImage());
            successCSVDialog.setBackground(Color.WHITE);
            successCSVDialog.setLocationRelativeTo(null);
            successCSVDialog.setVisible(true);
        } catch (FileNotFoundException fnf) {
            getComponents(fileNotFoundCSVDialog);
            fileNotFoundCSVDialog.setIconImage(favicon.getImage());
            fileNotFoundCSVDialog.setBackground(Color.WHITE);
            fileNotFoundCSVDialog.setLocationRelativeTo(null);
            fileNotFoundCSVDialog.setVisible(true);
        } catch (IOException | ParseException exception) {
            getComponents(internalErrorCSVDialog);
            internalErrorCSVDialog.setIconImage(favicon.getImage());
            internalErrorCSVDialog.setBackground(Color.WHITE);
            internalErrorCSVDialog.setLocationRelativeTo(null);
            internalErrorCSVDialog.setVisible(true);
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException io) {
                getComponents(internalErrorCSVDialog);
                internalErrorCSVDialog.setIconImage(favicon.getImage());
                internalErrorCSVDialog.setBackground(Color.WHITE);
                internalErrorCSVDialog.setLocationRelativeTo(null);
                internalErrorCSVDialog.setVisible(true);
            }
        }
    }
        
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainWheelStyleButtonGroup = new javax.swing.ButtonGroup();
        mainExtColorButtonGroup = new javax.swing.ButtonGroup();
        mainIntColorButtonGroup = new javax.swing.ButtonGroup();
        mainTransButtonGroup = new javax.swing.ButtonGroup();
        mainImageSelectorGroup = new javax.swing.ButtonGroup();
        overviewImageSelectorGroup = new javax.swing.ButtonGroup();
        overviewDialog = new javax.swing.JDialog();
        overviewCaymanTitleLabel = new javax.swing.JLabel();
        overviewCaymanDisplayLabel = new javax.swing.JLabel();
        overviewImageSelect1Radio = new javax.swing.JRadioButton();
        overviewImageSelect2Radio = new javax.swing.JRadioButton();
        overviewImageSelect3Radio = new javax.swing.JRadioButton();
        overviewImageSelect4Radio = new javax.swing.JRadioButton();
        overviewImageSelect5Radio = new javax.swing.JRadioButton();
        overviewImageSelect6Radio = new javax.swing.JRadioButton();
        overviewBasePriceTitleLabel = new javax.swing.JLabel();
        overviewBasePriceDollarLabel = new javax.swing.JLabel();
        overviewEquipPriceTitleLabel = new javax.swing.JLabel();
        overviewEquipPriceDollarLabel = new javax.swing.JLabel();
        overviewDeliverPriceTitleLabel = new javax.swing.JLabel();
        overviewDeliverPriceDollarLabel = new javax.swing.JLabel();
        overviewTotalPriceTitleLabel = new javax.swing.JLabel();
        overviewTotalPriceDollarLabel = new javax.swing.JLabel();
        overviewGrayDescriptionPanel = new javax.swing.JPanel();
        descriptionLabel = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        overviewDescriptionPaintSeparator = new javax.swing.JSeparator();
        overviewExtColorTitleLabel = new javax.swing.JLabel();
        overviewPaintColorNameLabel = new javax.swing.JLabel();
        overviewPaintColorPriceLabel = new javax.swing.JLabel();
        overviewPaintWheelSeparator = new javax.swing.JSeparator();
        overviewWheelStyleTitleLabel = new javax.swing.JLabel();
        overviewWheelStyleNameLabel = new javax.swing.JLabel();
        overviewWheelStylePriceLabel = new javax.swing.JLabel();
        overviewWheelLeatherSeparator = new javax.swing.JSeparator();
        overviewIntColorTitleLabel = new javax.swing.JLabel();
        overviewLeatherColorNameLabel = new javax.swing.JLabel();
        overviewLeatherColorPriceLabel = new javax.swing.JLabel();
        overviewLeatherTransSeparator = new javax.swing.JSeparator();
        overviewTransTitleLabel = new javax.swing.JLabel();
        overviewTransNameLabel = new javax.swing.JLabel();
        overviewTransPriceLabel = new javax.swing.JLabel();
        overviewTransUpgradesSeparator = new javax.swing.JSeparator();
        overviewAddUpgradesTitleLabel = new javax.swing.JLabel();
        overviewAddUpgradesNameLabel = new javax.swing.JLabel();
        overviewAddUpgradesPriceLabel = new javax.swing.JLabel();
        overviewUpgradesButtonsSeparator = new javax.swing.JSeparator();
        saveAsPDFButton = new javax.swing.JButton();
        saveAsCSVButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        successCSVDialog = new javax.swing.JDialog();
        csvSuccessDialogIcon = new javax.swing.JLabel();
        csvSuccessMessage = new javax.swing.JLabel();
        csvSuccessOKButton = new javax.swing.JButton();
        fileNotFoundCSVDialog = new javax.swing.JDialog();
        csvFileNotFoundErrorIcon = new javax.swing.JLabel();
        csvFileNotFoundErrorMessage = new javax.swing.JLabel();
        csvFileNotFoundOKButton = new javax.swing.JButton();
        internalErrorCSVDialog = new javax.swing.JDialog();
        csvInternalErrorIcon = new javax.swing.JLabel();
        csvInternalErrorMessage = new javax.swing.JLabel();
        csvInternalErrorOKButton = new javax.swing.JButton();
        successPDFDialog = new javax.swing.JDialog();
        pdfSuccessDialogIcon = new javax.swing.JLabel();
        pdfSuccessMessage = new javax.swing.JLabel();
        pdfSuccessOKButton = new javax.swing.JButton();
        fileNotFoundPDFDialog = new javax.swing.JDialog();
        pdfFileNotFoundOKButton = new javax.swing.JButton();
        pdfFileNotFoundErrorIcon = new javax.swing.JLabel();
        pdfFileNotFoundErrorMessage = new javax.swing.JLabel();
        mainCaymanTitleLabel = new javax.swing.JLabel();
        mainCaymanDisplayLabel = new javax.swing.JLabel();
        mainImageSelect1Radio = new javax.swing.JRadioButton();
        mainImageSelect2Radio = new javax.swing.JRadioButton();
        mainImageSelect3Radio = new javax.swing.JRadioButton();
        mainImageSelect4Radio = new javax.swing.JRadioButton();
        mainImageSelect5Radio = new javax.swing.JRadioButton();
        mainImageSelect6Radio = new javax.swing.JRadioButton();
        mainBasePriceTitleLabel = new javax.swing.JLabel();
        mainBasePriceDollarLabel = new javax.swing.JLabel();
        mainEquipPriceTitleLabel = new javax.swing.JLabel();
        mainEquipPriceDollarLabel = new javax.swing.JLabel();
        mainDeliverPriceTitleLabel = new javax.swing.JLabel();
        mainDeliverPriceDollarLabel = new javax.swing.JLabel();
        mainTotalPriceTitleLabel = new javax.swing.JLabel();
        mainTotalPriceDollarLabel = new javax.swing.JLabel();
        mainExtColorLabel = new javax.swing.JLabel();
        mainGuardsRedPaintRadio = new javax.swing.JRadioButton();
        mainRacingYellowPaintRadio = new javax.swing.JRadioButton();
        mainCarreraWhitePaintRadio = new javax.swing.JRadioButton();
        mainJetBlackPaintRadio = new javax.swing.JRadioButton();
        mainMiamiBluePaintRadio = new javax.swing.JRadioButton();
        mainPaintWheelSeparator = new javax.swing.JSeparator();
        mainWheelStyleLabel = new javax.swing.JLabel();
        mainCaymanSWheelRadio = new javax.swing.JRadioButton();
        mainCarreraSWheelRadio = new javax.swing.JRadioButton();
        main911TurboWheelRadio = new javax.swing.JRadioButton();
        mainWheelLeatherSeparator = new javax.swing.JSeparator();
        mainIntColorLabel = new javax.swing.JLabel();
        mainBlackLeatherRadio = new javax.swing.JRadioButton();
        mainBordeauxRedLeatherRadio = new javax.swing.JRadioButton();
        mainEspressoLeatherRadio = new javax.swing.JRadioButton();
        mainLeatherTransSeparator = new javax.swing.JSeparator();
        mainTransLabel = new javax.swing.JLabel();
        mainManualTransRadio = new javax.swing.JRadioButton();
        mainPDKTransRadio = new javax.swing.JRadioButton();
        mainTransUpgradesSeparator = new javax.swing.JSeparator();
        mainAddUpgradesLabel = new javax.swing.JLabel();
        mainSeatHeatCheckBox = new javax.swing.JCheckBox();
        mainPASMCheckBox = new javax.swing.JCheckBox();
        mainBoseCheckBox = new javax.swing.JCheckBox();
        mainNavCheckBox = new javax.swing.JCheckBox();
        mainUpgradesOverviewSeparator = new javax.swing.JSeparator();
        mainShowOverviewButton = new javax.swing.JButton();

        overviewDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        overviewDialog.setTitle("Your Specification Summary");
        overviewDialog.setIconImage(null);
        overviewDialog.setResizable(false);
        overviewDialog.setSize(new java.awt.Dimension(1100, 630));

        overviewCaymanTitleLabel.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        overviewCaymanTitleLabel.setText("2018 Porsche 718 Cayman S");

        overviewCaymanDisplayLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/cayman-model/default-config/ext-1.jpg"))); // NOI18N

        overviewImageSelectorGroup.add(overviewImageSelect1Radio);
        overviewImageSelect1Radio.setSelected(true);
        overviewImageSelect1Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        overviewImageSelect1Radio.setRolloverEnabled(false);
        overviewImageSelect1Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        overviewImageSelect1Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overviewImageSelect1RadioActionPerformed(evt);
            }
        });

        overviewImageSelectorGroup.add(overviewImageSelect2Radio);
        overviewImageSelect2Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        overviewImageSelect2Radio.setRolloverEnabled(false);
        overviewImageSelect2Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        overviewImageSelect2Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overviewImageSelect2RadioActionPerformed(evt);
            }
        });

        overviewImageSelectorGroup.add(overviewImageSelect3Radio);
        overviewImageSelect3Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        overviewImageSelect3Radio.setRolloverEnabled(false);
        overviewImageSelect3Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        overviewImageSelect3Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overviewImageSelect3RadioActionPerformed(evt);
            }
        });

        overviewImageSelectorGroup.add(overviewImageSelect4Radio);
        overviewImageSelect4Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        overviewImageSelect4Radio.setRolloverEnabled(false);
        overviewImageSelect4Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        overviewImageSelect4Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overviewImageSelect4RadioActionPerformed(evt);
            }
        });

        overviewImageSelectorGroup.add(overviewImageSelect5Radio);
        overviewImageSelect5Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        overviewImageSelect5Radio.setRolloverEnabled(false);
        overviewImageSelect5Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        overviewImageSelect5Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overviewImageSelect5RadioActionPerformed(evt);
            }
        });

        overviewImageSelectorGroup.add(overviewImageSelect6Radio);
        overviewImageSelect6Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        overviewImageSelect6Radio.setRolloverEnabled(false);
        overviewImageSelect6Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        overviewImageSelect6Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overviewImageSelect6RadioActionPerformed(evt);
            }
        });

        overviewBasePriceTitleLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        overviewBasePriceTitleLabel.setText("Base Price:");

        overviewBasePriceDollarLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        overviewBasePriceDollarLabel.setText("$67,700");

        overviewEquipPriceTitleLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        overviewEquipPriceTitleLabel.setText("Price for Equipment:");

        overviewEquipPriceDollarLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        overviewEquipPriceDollarLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        overviewEquipPriceDollarLabel.setText("$0");
        overviewEquipPriceDollarLabel.setAutoscrolls(true);

        overviewDeliverPriceTitleLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        overviewDeliverPriceTitleLabel.setText("Delivery, Processing, and Handling Fee:");

        overviewDeliverPriceDollarLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        overviewDeliverPriceDollarLabel.setText("$1,050");

        overviewTotalPriceTitleLabel.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        overviewTotalPriceTitleLabel.setText("Total Price:");

        overviewTotalPriceDollarLabel.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        overviewTotalPriceDollarLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        overviewTotalPriceDollarLabel.setText("$68,750");

        descriptionLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        descriptionLabel.setText("Description");

        priceLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        priceLabel.setText("Price");

        javax.swing.GroupLayout overviewGrayDescriptionPanelLayout = new javax.swing.GroupLayout(overviewGrayDescriptionPanel);
        overviewGrayDescriptionPanel.setLayout(overviewGrayDescriptionPanelLayout);
        overviewGrayDescriptionPanelLayout.setHorizontalGroup(
            overviewGrayDescriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewGrayDescriptionPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(descriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
                .addComponent(priceLabel)
                .addGap(36, 36, 36))
        );
        overviewGrayDescriptionPanelLayout.setVerticalGroup(
            overviewGrayDescriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewGrayDescriptionPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(overviewGrayDescriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionLabel)
                    .addComponent(priceLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        overviewExtColorTitleLabel.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        overviewExtColorTitleLabel.setText("Exterior Color");

        overviewPaintColorNameLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewPaintColorNameLabel.setText("Guards Red");

        overviewPaintColorPriceLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewPaintColorPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        overviewPaintColorPriceLabel.setText("$0");

        overviewWheelStyleTitleLabel.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        overviewWheelStyleTitleLabel.setText("Wheel Style");

        overviewWheelStyleNameLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewWheelStyleNameLabel.setText("19\" Cayman S Wheels");

        overviewWheelStylePriceLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewWheelStylePriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        overviewWheelStylePriceLabel.setText("$0");

        overviewIntColorTitleLabel.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        overviewIntColorTitleLabel.setText("Interior Color");

        overviewLeatherColorNameLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewLeatherColorNameLabel.setText("Standard Black Interior");

        overviewLeatherColorPriceLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewLeatherColorPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        overviewLeatherColorPriceLabel.setText("$0");

        overviewTransTitleLabel.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        overviewTransTitleLabel.setText("Transmission");

        overviewTransNameLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewTransNameLabel.setText("Manual");

        overviewTransPriceLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewTransPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        overviewTransPriceLabel.setText("$0");

        overviewAddUpgradesTitleLabel.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        overviewAddUpgradesTitleLabel.setText("Additional Upgrades");

        overviewAddUpgradesNameLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewAddUpgradesNameLabel.setText("N/A");
        overviewAddUpgradesNameLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        overviewAddUpgradesPriceLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        overviewAddUpgradesPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        overviewAddUpgradesPriceLabel.setText("N/A");
        overviewAddUpgradesPriceLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        saveAsPDFButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/save-as-pdf-button.png"))); // NOI18N
        saveAsPDFButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsPDFButtonActionPerformed(evt);
            }
        });

        saveAsCSVButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/save-as-csv-button.png"))); // NOI18N
        saveAsCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsCSVButtonActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/cancel-button.png"))); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout overviewDialogLayout = new javax.swing.GroupLayout(overviewDialog.getContentPane());
        overviewDialog.getContentPane().setLayout(overviewDialogLayout);
        overviewDialogLayout.setHorizontalGroup(
            overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewDialogLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(overviewCaymanTitleLabel)
                    .addGroup(overviewDialogLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(overviewDialogLayout.createSequentialGroup()
                                .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(overviewTotalPriceTitleLabel)
                                    .addComponent(overviewBasePriceTitleLabel)
                                    .addComponent(overviewDeliverPriceTitleLabel)
                                    .addComponent(overviewEquipPriceTitleLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(overviewDeliverPriceDollarLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(overviewTotalPriceDollarLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(overviewEquipPriceDollarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(overviewBasePriceDollarLabel, javax.swing.GroupLayout.Alignment.TRAILING))))
                            .addComponent(overviewCaymanDisplayLabel)))
                    .addGroup(overviewDialogLayout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(overviewImageSelect1Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewImageSelect2Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewImageSelect3Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewImageSelect4Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewImageSelect5Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewImageSelect6Radio)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(overviewDescriptionPaintSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(overviewTransUpgradesSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(overviewDialogLayout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(overviewDialogLayout.createSequentialGroup()
                                    .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(overviewDialogLayout.createSequentialGroup()
                                            .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(overviewWheelStyleNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(overviewPaintColorNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(overviewLeatherColorNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(overviewTransNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(overviewPaintColorPriceLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(overviewWheelStylePriceLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(overviewLeatherColorPriceLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(overviewTransPriceLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(overviewDialogLayout.createSequentialGroup()
                                            .addComponent(overviewAddUpgradesNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(overviewAddUpgradesPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(37, 37, 37))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, overviewDialogLayout.createSequentialGroup()
                                    .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(overviewExtColorTitleLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(overviewWheelStyleTitleLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(overviewIntColorTitleLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(overviewTransTitleLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(overviewAddUpgradesTitleLabel, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGap(0, 0, Short.MAX_VALUE))))
                        .addComponent(overviewGrayDescriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(overviewPaintWheelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(overviewWheelLeatherSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(overviewLeatherTransSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(overviewUpgradesButtonsSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(overviewDialogLayout.createSequentialGroup()
                        .addComponent(saveAsPDFButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveAsCSVButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(77, 77, 77))
        );
        overviewDialogLayout.setVerticalGroup(
            overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, overviewDialogLayout.createSequentialGroup()
                        .addComponent(overviewCaymanTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(overviewCaymanDisplayLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(overviewImageSelect2Radio)
                                    .addComponent(overviewImageSelect3Radio)
                                    .addComponent(overviewImageSelect4Radio, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(overviewImageSelect5Radio, javax.swing.GroupLayout.Alignment.LEADING))
                                .addComponent(overviewImageSelect1Radio))
                            .addComponent(overviewImageSelect6Radio))
                        .addGap(43, 43, 43)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(overviewDialogLayout.createSequentialGroup()
                                .addComponent(overviewBasePriceTitleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(overviewEquipPriceTitleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(overviewDeliverPriceTitleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(overviewTotalPriceTitleLabel))
                            .addGroup(overviewDialogLayout.createSequentialGroup()
                                .addComponent(overviewBasePriceDollarLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(overviewEquipPriceDollarLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(overviewDeliverPriceDollarLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(overviewTotalPriceDollarLabel)))
                        .addGap(89, 89, 89))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, overviewDialogLayout.createSequentialGroup()
                        .addComponent(overviewGrayDescriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewDescriptionPaintSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewExtColorTitleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(overviewPaintColorNameLabel)
                            .addComponent(overviewPaintColorPriceLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewPaintWheelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(overviewWheelStyleTitleLabel)
                        .addGap(11, 11, 11)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(overviewWheelStyleNameLabel)
                            .addComponent(overviewWheelStylePriceLabel))
                        .addGap(7, 7, 7)
                        .addComponent(overviewWheelLeatherSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewIntColorTitleLabel)
                        .addGap(11, 11, 11)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(overviewLeatherColorNameLabel)
                            .addComponent(overviewLeatherColorPriceLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overviewLeatherTransSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(overviewTransTitleLabel)
                        .addGap(11, 11, 11)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(overviewTransNameLabel)
                            .addComponent(overviewTransPriceLabel))
                        .addGap(8, 8, 8)
                        .addComponent(overviewTransUpgradesSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(overviewAddUpgradesTitleLabel)
                        .addGap(11, 11, 11)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(overviewAddUpgradesPriceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(overviewAddUpgradesNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(overviewUpgradesButtonsSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(overviewDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(saveAsPDFButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(saveAsCSVButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(92, 92, 92))))
        );

        successCSVDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        successCSVDialog.setTitle("File Successfully Saved");
        successCSVDialog.setIconImage(null);
        successCSVDialog.setResizable(false);
        successCSVDialog.setSize(new java.awt.Dimension(1040, 175));

        csvSuccessDialogIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/icons/success-dialog-icon.png"))); // NOI18N
        csvSuccessDialogIcon.setText("jLabel1");

        csvSuccessMessage.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        csvSuccessMessage.setText("Success! Your specification has been saved to the current user's home directory as a CSV file named \"cayman-spec.csv\"");

        csvSuccessOKButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/ok-button.png"))); // NOI18N
        csvSuccessOKButton.setVerifyInputWhenFocusTarget(false);
        csvSuccessOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvSuccessOKButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout successCSVDialogLayout = new javax.swing.GroupLayout(successCSVDialog.getContentPane());
        successCSVDialog.getContentPane().setLayout(successCSVDialogLayout);
        successCSVDialogLayout.setHorizontalGroup(
            successCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(successCSVDialogLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(csvSuccessDialogIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(csvSuccessMessage)
                .addContainerGap(61, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, successCSVDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(csvSuccessOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(422, 422, 422))
        );
        successCSVDialogLayout.setVerticalGroup(
            successCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(successCSVDialogLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(successCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(csvSuccessDialogIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(csvSuccessMessage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(csvSuccessOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        fileNotFoundCSVDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        fileNotFoundCSVDialog.setTitle("Error: File Not Found");
        fileNotFoundCSVDialog.setResizable(false);
        fileNotFoundCSVDialog.setSize(new java.awt.Dimension(1040, 175));

        csvFileNotFoundErrorIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/icons/error-dialog-icon.png"))); // NOI18N
        csvFileNotFoundErrorIcon.setText("jLabel1");

        csvFileNotFoundErrorMessage.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        csvFileNotFoundErrorMessage.setText("Error! The file you are trying to access is currently being used by another operation or cannot be found. Please try again.");

        csvFileNotFoundOKButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/ok-button.png"))); // NOI18N
        csvFileNotFoundOKButton.setVerifyInputWhenFocusTarget(false);
        csvFileNotFoundOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvFileNotFoundOKButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fileNotFoundCSVDialogLayout = new javax.swing.GroupLayout(fileNotFoundCSVDialog.getContentPane());
        fileNotFoundCSVDialog.getContentPane().setLayout(fileNotFoundCSVDialogLayout);
        fileNotFoundCSVDialogLayout.setHorizontalGroup(
            fileNotFoundCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileNotFoundCSVDialogLayout.createSequentialGroup()
                .addGroup(fileNotFoundCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fileNotFoundCSVDialogLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(csvFileNotFoundErrorIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(csvFileNotFoundErrorMessage))
                    .addGroup(fileNotFoundCSVDialogLayout.createSequentialGroup()
                        .addGap(442, 442, 442)
                        .addComponent(csvFileNotFoundOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        fileNotFoundCSVDialogLayout.setVerticalGroup(
            fileNotFoundCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileNotFoundCSVDialogLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(fileNotFoundCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(csvFileNotFoundErrorIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(csvFileNotFoundErrorMessage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(csvFileNotFoundOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        internalErrorCSVDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        internalErrorCSVDialog.setTitle("Internal Error");
        internalErrorCSVDialog.setResizable(false);
        internalErrorCSVDialog.setSize(new java.awt.Dimension(400, 175));

        csvInternalErrorIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/icons/error-dialog-icon.png"))); // NOI18N
        csvInternalErrorIcon.setText("jLabel1");

        csvInternalErrorMessage.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        csvInternalErrorMessage.setText("Internal error. Please try again.");

        csvInternalErrorOKButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/ok-button.png"))); // NOI18N
        csvInternalErrorOKButton.setVerifyInputWhenFocusTarget(false);
        csvInternalErrorOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvInternalErrorOKButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout internalErrorCSVDialogLayout = new javax.swing.GroupLayout(internalErrorCSVDialog.getContentPane());
        internalErrorCSVDialog.getContentPane().setLayout(internalErrorCSVDialogLayout);
        internalErrorCSVDialogLayout.setHorizontalGroup(
            internalErrorCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(internalErrorCSVDialogLayout.createSequentialGroup()
                .addGroup(internalErrorCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(internalErrorCSVDialogLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(csvInternalErrorIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(csvInternalErrorMessage))
                    .addGroup(internalErrorCSVDialogLayout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(csvInternalErrorOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        internalErrorCSVDialogLayout.setVerticalGroup(
            internalErrorCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(internalErrorCSVDialogLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(internalErrorCSVDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(csvInternalErrorIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(csvInternalErrorMessage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(csvInternalErrorOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        successPDFDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        successPDFDialog.setTitle("File Successfully Saved");
        successPDFDialog.setResizable(false);
        successPDFDialog.setSize(new java.awt.Dimension(1040, 175));

        pdfSuccessDialogIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/icons/success-dialog-icon.png"))); // NOI18N
        pdfSuccessDialogIcon.setText("jLabel1");

        pdfSuccessMessage.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        pdfSuccessMessage.setText("Success! Your specification has been saved to the current user's home directory as a PDF file named \"cayman-spec.pdf\"");

        pdfSuccessOKButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/ok-button.png"))); // NOI18N
        pdfSuccessOKButton.setVerifyInputWhenFocusTarget(false);
        pdfSuccessOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdfSuccessOKButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout successPDFDialogLayout = new javax.swing.GroupLayout(successPDFDialog.getContentPane());
        successPDFDialog.getContentPane().setLayout(successPDFDialogLayout);
        successPDFDialogLayout.setHorizontalGroup(
            successPDFDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(successPDFDialogLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(pdfSuccessDialogIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(pdfSuccessMessage)
                .addContainerGap(64, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, successPDFDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pdfSuccessOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(422, 422, 422))
        );
        successPDFDialogLayout.setVerticalGroup(
            successPDFDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(successPDFDialogLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(successPDFDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pdfSuccessDialogIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pdfSuccessMessage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pdfSuccessOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        fileNotFoundPDFDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        fileNotFoundPDFDialog.setTitle("Error: File Not Found");
        fileNotFoundPDFDialog.setResizable(false);
        fileNotFoundPDFDialog.setSize(new java.awt.Dimension(1040, 175));

        pdfFileNotFoundOKButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/ok-button.png"))); // NOI18N
        pdfFileNotFoundOKButton.setVerifyInputWhenFocusTarget(false);
        pdfFileNotFoundOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdfFileNotFoundOKButtonActionPerformed(evt);
            }
        });

        pdfFileNotFoundErrorIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/icons/error-dialog-icon.png"))); // NOI18N
        pdfFileNotFoundErrorIcon.setText("jLabel1");

        pdfFileNotFoundErrorMessage.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        pdfFileNotFoundErrorMessage.setText("Error! The file you are trying to access is currently being used by another operation or cannot be found. Please try again.");

        javax.swing.GroupLayout fileNotFoundPDFDialogLayout = new javax.swing.GroupLayout(fileNotFoundPDFDialog.getContentPane());
        fileNotFoundPDFDialog.getContentPane().setLayout(fileNotFoundPDFDialogLayout);
        fileNotFoundPDFDialogLayout.setHorizontalGroup(
            fileNotFoundPDFDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileNotFoundPDFDialogLayout.createSequentialGroup()
                .addGroup(fileNotFoundPDFDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fileNotFoundPDFDialogLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(pdfFileNotFoundErrorIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(pdfFileNotFoundErrorMessage))
                    .addGroup(fileNotFoundPDFDialogLayout.createSequentialGroup()
                        .addGap(442, 442, 442)
                        .addComponent(pdfFileNotFoundOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        fileNotFoundPDFDialogLayout.setVerticalGroup(
            fileNotFoundPDFDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileNotFoundPDFDialogLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(fileNotFoundPDFDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pdfFileNotFoundErrorIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pdfFileNotFoundErrorMessage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pdfFileNotFoundOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Porsche 718 Cayman S Configurator");
        setForeground(java.awt.Color.white);
        setName(""); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(1000, 650));

        mainCaymanTitleLabel.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        mainCaymanTitleLabel.setText("2018 Porsche 718 Cayman S");

        mainCaymanDisplayLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/cayman-model/default-config/ext-1.jpg"))); // NOI18N

        mainImageSelectorGroup.add(mainImageSelect1Radio);
        mainImageSelect1Radio.setSelected(true);
        mainImageSelect1Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        mainImageSelect1Radio.setRolloverEnabled(false);
        mainImageSelect1Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        mainImageSelect1Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainImageSelect1RadioActionPerformed(evt);
            }
        });

        mainImageSelectorGroup.add(mainImageSelect2Radio);
        mainImageSelect2Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        mainImageSelect2Radio.setRolloverEnabled(false);
        mainImageSelect2Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        mainImageSelect2Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainImageSelect2RadioActionPerformed(evt);
            }
        });

        mainImageSelectorGroup.add(mainImageSelect3Radio);
        mainImageSelect3Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        mainImageSelect3Radio.setRolloverEnabled(false);
        mainImageSelect3Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        mainImageSelect3Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainImageSelect3RadioActionPerformed(evt);
            }
        });

        mainImageSelectorGroup.add(mainImageSelect4Radio);
        mainImageSelect4Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        mainImageSelect4Radio.setRolloverEnabled(false);
        mainImageSelect4Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        mainImageSelect4Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainImageSelect4RadioActionPerformed(evt);
            }
        });

        mainImageSelectorGroup.add(mainImageSelect5Radio);
        mainImageSelect5Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        mainImageSelect5Radio.setRolloverEnabled(false);
        mainImageSelect5Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        mainImageSelect5Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainImageSelect5RadioActionPerformed(evt);
            }
        });

        mainImageSelectorGroup.add(mainImageSelect6Radio);
        mainImageSelect6Radio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-deselected.png"))); // NOI18N
        mainImageSelect6Radio.setRolloverEnabled(false);
        mainImageSelect6Radio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/image-select-radio-selected.png"))); // NOI18N
        mainImageSelect6Radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainImageSelect6RadioActionPerformed(evt);
            }
        });

        mainBasePriceTitleLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainBasePriceTitleLabel.setText("Base Price:");

        mainBasePriceDollarLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainBasePriceDollarLabel.setText("$67,700");

        mainEquipPriceTitleLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainEquipPriceTitleLabel.setText("Price for Equipment:");

        mainEquipPriceDollarLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainEquipPriceDollarLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainEquipPriceDollarLabel.setText("$0");
        mainEquipPriceDollarLabel.setAutoscrolls(true);

        mainDeliverPriceTitleLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainDeliverPriceTitleLabel.setText("Delivery, Processing, and Handling Fee:");

        mainDeliverPriceDollarLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainDeliverPriceDollarLabel.setText("$1,050");

        mainTotalPriceTitleLabel.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mainTotalPriceTitleLabel.setText("Total Price:");

        mainTotalPriceDollarLabel.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mainTotalPriceDollarLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainTotalPriceDollarLabel.setText("$68,750");

        mainExtColorLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        mainExtColorLabel.setText("Exterior Color");

        mainExtColorButtonGroup.add(mainGuardsRedPaintRadio);
        mainGuardsRedPaintRadio.setSelected(true);
        mainGuardsRedPaintRadio.setToolTipText("Guards Red - $0");
        mainGuardsRedPaintRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/not-selected/guards-red.PNG"))); // NOI18N
        mainGuardsRedPaintRadio.setRolloverEnabled(false);
        mainGuardsRedPaintRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/selected/guards-red.png"))); // NOI18N
        mainGuardsRedPaintRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainGuardsRedPaintRadioActionPerformed(evt);
            }
        });

        mainExtColorButtonGroup.add(mainRacingYellowPaintRadio);
        mainRacingYellowPaintRadio.setToolTipText("Racing Yellow - $0");
        mainRacingYellowPaintRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/not-selected/racing-yellow.PNG"))); // NOI18N
        mainRacingYellowPaintRadio.setRolloverEnabled(false);
        mainRacingYellowPaintRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/selected/racing-yellow.png"))); // NOI18N
        mainRacingYellowPaintRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainRacingYellowPaintRadioActionPerformed(evt);
            }
        });

        mainExtColorButtonGroup.add(mainCarreraWhitePaintRadio);
        mainCarreraWhitePaintRadio.setToolTipText("Carrera White Metallic - $650");
        mainCarreraWhitePaintRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/not-selected/carrera-white-metallic.PNG"))); // NOI18N
        mainCarreraWhitePaintRadio.setRolloverEnabled(false);
        mainCarreraWhitePaintRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/selected/carrera-white-metallic.png"))); // NOI18N
        mainCarreraWhitePaintRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainCarreraWhitePaintRadioActionPerformed(evt);
            }
        });

        mainExtColorButtonGroup.add(mainJetBlackPaintRadio);
        mainJetBlackPaintRadio.setToolTipText("Jet Black Metallic - $650");
        mainJetBlackPaintRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/not-selected/jet-black-metallic.PNG"))); // NOI18N
        mainJetBlackPaintRadio.setRolloverEnabled(false);
        mainJetBlackPaintRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/selected/jet-black-metallic.png"))); // NOI18N
        mainJetBlackPaintRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainJetBlackPaintRadioActionPerformed(evt);
            }
        });

        mainExtColorButtonGroup.add(mainMiamiBluePaintRadio);
        mainMiamiBluePaintRadio.setToolTipText("Miami Blue - $2,580");
        mainMiamiBluePaintRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/not-selected/miami-blue.PNG"))); // NOI18N
        mainMiamiBluePaintRadio.setRolloverEnabled(false);
        mainMiamiBluePaintRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/paint-samples/selected/miami-blue.png"))); // NOI18N
        mainMiamiBluePaintRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainMiamiBluePaintRadioActionPerformed(evt);
            }
        });

        mainWheelStyleLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        mainWheelStyleLabel.setText("Wheel Style");

        mainWheelStyleButtonGroup.add(mainCaymanSWheelRadio);
        mainCaymanSWheelRadio.setSelected(true);
        mainCaymanSWheelRadio.setToolTipText("19\" Cayman S Wheels - $0");
        mainCaymanSWheelRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/wheel-types/not-selected/19-cayman-s.PNG"))); // NOI18N
        mainCaymanSWheelRadio.setRolloverEnabled(false);
        mainCaymanSWheelRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/wheel-types/selected/19-cayman-s.png"))); // NOI18N
        mainCaymanSWheelRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainCaymanSWheelRadioActionPerformed(evt);
            }
        });

        mainWheelStyleButtonGroup.add(mainCarreraSWheelRadio);
        mainCarreraSWheelRadio.setToolTipText("20\" 911 Turbo Wheels - $3,570");
        mainCarreraSWheelRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/wheel-types/not-selected/20-911-turbo.PNG"))); // NOI18N
        mainCarreraSWheelRadio.setRolloverEnabled(false);
        mainCarreraSWheelRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/wheel-types/selected/20-911-turbo.png"))); // NOI18N
        mainCarreraSWheelRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainCarreraSWheelRadioActionPerformed(evt);
            }
        });

        mainWheelStyleButtonGroup.add(main911TurboWheelRadio);
        main911TurboWheelRadio.setToolTipText("20\" Carrera S Wheels - $1,590");
        main911TurboWheelRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/wheel-types/not-selected/20-carrera-s.PNG"))); // NOI18N
        main911TurboWheelRadio.setRolloverEnabled(false);
        main911TurboWheelRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/wheel-types/selected/20-carrera-s.png"))); // NOI18N
        main911TurboWheelRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main911TurboWheelRadioActionPerformed(evt);
            }
        });

        mainIntColorLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        mainIntColorLabel.setText("Interior Color");

        mainIntColorButtonGroup.add(mainBlackLeatherRadio);
        mainBlackLeatherRadio.setSelected(true);
        mainBlackLeatherRadio.setToolTipText("Standard Interior in Black - $0");
        mainBlackLeatherRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/leather-samples/not-selected/black.PNG"))); // NOI18N
        mainBlackLeatherRadio.setRolloverEnabled(false);
        mainBlackLeatherRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/leather-samples/selected/black.png"))); // NOI18N
        mainBlackLeatherRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainBlackLeatherRadioActionPerformed(evt);
            }
        });

        mainIntColorButtonGroup.add(mainBordeauxRedLeatherRadio);
        mainBordeauxRedLeatherRadio.setToolTipText("Leather Interior in Bordeaux Red - $2,950");
        mainBordeauxRedLeatherRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/leather-samples/not-selected/bordeaux-red.PNG"))); // NOI18N
        mainBordeauxRedLeatherRadio.setRolloverEnabled(false);
        mainBordeauxRedLeatherRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/leather-samples/selected/bordeaux-red.png"))); // NOI18N
        mainBordeauxRedLeatherRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainBordeauxRedLeatherRadioActionPerformed(evt);
            }
        });

        mainIntColorButtonGroup.add(mainEspressoLeatherRadio);
        mainEspressoLeatherRadio.setToolTipText("Natural Leather Interior in Espresso/Cognac - $4,470");
        mainEspressoLeatherRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/leather-samples/not-selected/espresso.PNG"))); // NOI18N
        mainEspressoLeatherRadio.setRolloverEnabled(false);
        mainEspressoLeatherRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/leather-samples/selected/espresso.png"))); // NOI18N
        mainEspressoLeatherRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainEspressoLeatherRadioActionPerformed(evt);
            }
        });

        mainTransLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        mainTransLabel.setText("Transmission");

        mainTransButtonGroup.add(mainManualTransRadio);
        mainManualTransRadio.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainManualTransRadio.setSelected(true);
        mainManualTransRadio.setText("6-speed Manual - $0");
        mainManualTransRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/radio-button-deselected.png"))); // NOI18N
        mainManualTransRadio.setRolloverEnabled(false);
        mainManualTransRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/radio-button-selected.png"))); // NOI18N
        mainManualTransRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainManualTransRadioActionPerformed(evt);
            }
        });

        mainTransButtonGroup.add(mainPDKTransRadio);
        mainPDKTransRadio.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainPDKTransRadio.setText("7-speed Porsche Doppelkupplung (PDK) - $3,210");
        mainPDKTransRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/radio-button-deselected.png"))); // NOI18N
        mainPDKTransRadio.setRolloverEnabled(false);
        mainPDKTransRadio.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/radio-button-selected.png"))); // NOI18N
        mainPDKTransRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainPDKTransRadioActionPerformed(evt);
            }
        });

        mainAddUpgradesLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        mainAddUpgradesLabel.setText("Additional Upgrades");

        mainSeatHeatCheckBox.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainSeatHeatCheckBox.setText("Seat Heating - $530");
        mainSeatHeatCheckBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/checkbox-deselected.png"))); // NOI18N
        mainSeatHeatCheckBox.setRolloverEnabled(false);
        mainSeatHeatCheckBox.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/checkbox-selected.png"))); // NOI18N
        mainSeatHeatCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainSeatHeatCheckBoxActionPerformed(evt);
            }
        });

        mainPASMCheckBox.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainPASMCheckBox.setText("Porsche Active Stability Management (PASM) - $1,790");
        mainPASMCheckBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/checkbox-deselected.png"))); // NOI18N
        mainPASMCheckBox.setRolloverEnabled(false);
        mainPASMCheckBox.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/checkbox-selected.png"))); // NOI18N
        mainPASMCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainPASMCheckBoxActionPerformed(evt);
            }
        });

        mainBoseCheckBox.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainBoseCheckBox.setText("BOSE® Surround Sound System - $990 ");
        mainBoseCheckBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/checkbox-deselected.png"))); // NOI18N
        mainBoseCheckBox.setRolloverEnabled(false);
        mainBoseCheckBox.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/checkbox-selected.png"))); // NOI18N
        mainBoseCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainBoseCheckBoxActionPerformed(evt);
            }
        });

        mainNavCheckBox.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mainNavCheckBox.setText("Navigation Module - $1,780");
        mainNavCheckBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/checkbox-deselected.png"))); // NOI18N
        mainNavCheckBox.setRolloverEnabled(false);
        mainNavCheckBox.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/checkbox-selected.png"))); // NOI18N
        mainNavCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainNavCheckBoxActionPerformed(evt);
            }
        });

        mainShowOverviewButton.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        mainShowOverviewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/configurator/images/ui-items/buttons/show-overview-button.png"))); // NOI18N
        mainShowOverviewButton.setBorder(null);
        mainShowOverviewButton.setContentAreaFilled(false);
        mainShowOverviewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainShowOverviewButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainCaymanTitleLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mainTotalPriceTitleLabel)
                                    .addComponent(mainBasePriceTitleLabel)
                                    .addComponent(mainDeliverPriceTitleLabel)
                                    .addComponent(mainEquipPriceTitleLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mainDeliverPriceDollarLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(mainTotalPriceDollarLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(mainEquipPriceDollarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(mainBasePriceDollarLabel, javax.swing.GroupLayout.Alignment.TRAILING))))
                            .addComponent(mainCaymanDisplayLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(mainImageSelect1Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainImageSelect2Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainImageSelect3Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainImageSelect4Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainImageSelect5Radio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainImageSelect6Radio)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mainAddUpgradesLabel)
                            .addComponent(mainSeatHeatCheckBox)
                            .addComponent(mainTransLabel)
                            .addComponent(mainWheelStyleLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mainCaymanSWheelRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(main911TurboWheelRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainCarreraSWheelRadio))
                            .addComponent(mainIntColorLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(mainGuardsRedPaintRadio)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mainRacingYellowPaintRadio))
                                    .addComponent(mainExtColorLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainCarreraWhitePaintRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainJetBlackPaintRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainMiamiBluePaintRadio))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mainBlackLeatherRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainBordeauxRedLeatherRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainEspressoLeatherRadio))
                            .addComponent(mainManualTransRadio)
                            .addComponent(mainPDKTransRadio)
                            .addComponent(mainBoseCheckBox)
                            .addComponent(mainNavCheckBox)
                            .addComponent(mainPASMCheckBox)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(mainPaintWheelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(mainUpgradesOverviewSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mainTransUpgradesSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mainLeatherTransSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mainWheelLeatherSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(71, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainShowOverviewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(188, 188, 188))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(mainExtColorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mainJetBlackPaintRadio)
                            .addComponent(mainMiamiBluePaintRadio)
                            .addComponent(mainGuardsRedPaintRadio)
                            .addComponent(mainRacingYellowPaintRadio)
                            .addComponent(mainCarreraWhitePaintRadio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainPaintWheelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainWheelStyleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(main911TurboWheelRadio)
                            .addComponent(mainCaymanSWheelRadio)
                            .addComponent(mainCarreraSWheelRadio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainWheelLeatherSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainIntColorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mainBlackLeatherRadio)
                            .addComponent(mainBordeauxRedLeatherRadio)
                            .addComponent(mainEspressoLeatherRadio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainLeatherTransSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainTransLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainManualTransRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainPDKTransRadio))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(mainCaymanTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(mainCaymanDisplayLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(mainImageSelect2Radio)
                                .addComponent(mainImageSelect3Radio)
                                .addComponent(mainImageSelect4Radio, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(mainImageSelect5Radio, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(mainImageSelect6Radio, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(mainImageSelect1Radio))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainTransUpgradesSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(mainAddUpgradesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mainSeatHeatCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainPASMCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainBoseCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainNavCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(mainUpgradesOverviewSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(mainShowOverviewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mainBasePriceTitleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainEquipPriceTitleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainDeliverPriceTitleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainTotalPriceTitleLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mainBasePriceDollarLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainEquipPriceDollarLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainDeliverPriceDollarLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainTotalPriceDollarLabel)))
                        .addGap(92, 92, 92))))
        );

        getAccessibleContext().setAccessibleName("");
        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    //Paint color radio button action handlers
    private void mainGuardsRedPaintRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainGuardsRedPaintRadioActionPerformed
        cayman.setExtColor("Guards Red", 0, "guards-red");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainGuardsRedPaintRadioActionPerformed

    private void mainRacingYellowPaintRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainRacingYellowPaintRadioActionPerformed
        cayman.setExtColor("Racing Yellow", 0, "racing-yellow");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainRacingYellowPaintRadioActionPerformed

    private void mainCarreraWhitePaintRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainCarreraWhitePaintRadioActionPerformed
        cayman.setExtColor("Carrera White Metallic", 650, "carrera-white-metallic");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainCarreraWhitePaintRadioActionPerformed

    private void mainJetBlackPaintRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainJetBlackPaintRadioActionPerformed
        cayman.setExtColor("Jet Black Metallic", 650, "jet-black-metallic");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainJetBlackPaintRadioActionPerformed

    private void mainMiamiBluePaintRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainMiamiBluePaintRadioActionPerformed
        cayman.setExtColor("Miami Blue", 2580, "miami-blue");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainMiamiBluePaintRadioActionPerformed

    //Wheel styles radio button action handlers
    private void mainCaymanSWheelRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainCaymanSWheelRadioActionPerformed
        cayman.setWheelStyle("19\" Cayman S Wheels", 0, "19-cayman-s-wheels");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainCaymanSWheelRadioActionPerformed

    private void main911TurboWheelRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main911TurboWheelRadioActionPerformed
        cayman.setWheelStyle("20\" Carrera S Wheels", 1590, "20-carrera-s-wheels");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_main911TurboWheelRadioActionPerformed

    private void mainCarreraSWheelRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainCarreraSWheelRadioActionPerformed
        cayman.setWheelStyle("20\" 911 Turbo Wheels", 3570, "20-911-turbo-wheels");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainCarreraSWheelRadioActionPerformed

    //Interior colors radio button action handlers
    private void mainBlackLeatherRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainBlackLeatherRadioActionPerformed
        cayman.setIntColor("Standard Interior in Black", 0, "black-interior");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainBlackLeatherRadioActionPerformed

    private void mainBordeauxRedLeatherRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainBordeauxRedLeatherRadioActionPerformed
        cayman.setIntColor("Leather Interior in Bordeaux Red", 2950, "bordeaux-red-interior");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainBordeauxRedLeatherRadioActionPerformed

    private void mainEspressoLeatherRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainEspressoLeatherRadioActionPerformed
        cayman.setIntColor("Natural Leather Interior in Espresso/Cognac", 4470, "espresso-interior");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainEspressoLeatherRadioActionPerformed

    private void mainManualTransRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainManualTransRadioActionPerformed
        cayman.setTrans("6-speed Manual", 0, "manual-trans");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainManualTransRadioActionPerformed

    //Transmission types radio button action handlers
    private void mainPDKTransRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainPDKTransRadioActionPerformed
        cayman.setTrans("7-speed Porsche Doppelkupplung (PDK)", 3210, "pdk");
        setPriceFields();
        setImagePath("main window");
    }//GEN-LAST:event_mainPDKTransRadioActionPerformed

    //Model image angle selector radio button action handlers
    private void mainImageSelect1RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainImageSelect1RadioActionPerformed
        setImagePath("main window");
    }//GEN-LAST:event_mainImageSelect1RadioActionPerformed

    private void mainImageSelect2RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainImageSelect2RadioActionPerformed
        setImagePath("main window");
    }//GEN-LAST:event_mainImageSelect2RadioActionPerformed

    private void mainImageSelect3RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainImageSelect3RadioActionPerformed
        setImagePath("main window");
    }//GEN-LAST:event_mainImageSelect3RadioActionPerformed

    private void mainImageSelect4RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainImageSelect4RadioActionPerformed
        setImagePath("main window");
    }//GEN-LAST:event_mainImageSelect4RadioActionPerformed

    private void mainImageSelect5RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainImageSelect5RadioActionPerformed
        setImagePath("main window");
    }//GEN-LAST:event_mainImageSelect5RadioActionPerformed

    private void mainImageSelect6RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainImageSelect6RadioActionPerformed
        setImagePath("main window");
    }//GEN-LAST:event_mainImageSelect6RadioActionPerformed

    //Additional upgrades radio button action handlers
    private void mainSeatHeatCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainSeatHeatCheckBoxActionPerformed
        setPriceFields();
    }//GEN-LAST:event_mainSeatHeatCheckBoxActionPerformed

    private void mainPASMCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainPASMCheckBoxActionPerformed
        setPriceFields();
    }//GEN-LAST:event_mainPASMCheckBoxActionPerformed

    private void mainBoseCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainBoseCheckBoxActionPerformed
        setPriceFields();
    }//GEN-LAST:event_mainBoseCheckBoxActionPerformed

    private void mainNavCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainNavCheckBoxActionPerformed
        setPriceFields();
    }//GEN-LAST:event_mainNavCheckBoxActionPerformed

    private void mainShowOverviewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainShowOverviewButtonActionPerformed
        initOverviewDialog();
    }//GEN-LAST:event_mainShowOverviewButtonActionPerformed

    private void overviewImageSelect1RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overviewImageSelect1RadioActionPerformed
        setImagePath("overview window");
    }//GEN-LAST:event_overviewImageSelect1RadioActionPerformed

    private void overviewImageSelect2RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overviewImageSelect2RadioActionPerformed
        setImagePath("overview window");
    }//GEN-LAST:event_overviewImageSelect2RadioActionPerformed

    private void overviewImageSelect3RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overviewImageSelect3RadioActionPerformed
        setImagePath("overview window");
    }//GEN-LAST:event_overviewImageSelect3RadioActionPerformed

    private void overviewImageSelect4RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overviewImageSelect4RadioActionPerformed
        setImagePath("overview window");
    }//GEN-LAST:event_overviewImageSelect4RadioActionPerformed

    private void overviewImageSelect5RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overviewImageSelect5RadioActionPerformed
        setImagePath("overview window");
    }//GEN-LAST:event_overviewImageSelect5RadioActionPerformed

    private void overviewImageSelect6RadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overviewImageSelect6RadioActionPerformed
        setImagePath("overview window");
    }//GEN-LAST:event_overviewImageSelect6RadioActionPerformed

    private void saveAsPDFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsPDFButtonActionPerformed
        printDialogToPDF();
    }//GEN-LAST:event_saveAsPDFButtonActionPerformed

    private void saveAsCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsCSVButtonActionPerformed
        printDialogToCSV();
    }//GEN-LAST:event_saveAsCSVButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        overviewDialog.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void csvSuccessOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvSuccessOKButtonActionPerformed
        successCSVDialog.dispose();
    }//GEN-LAST:event_csvSuccessOKButtonActionPerformed

    private void csvFileNotFoundOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvFileNotFoundOKButtonActionPerformed
        fileNotFoundCSVDialog.dispose();
    }//GEN-LAST:event_csvFileNotFoundOKButtonActionPerformed

    private void csvInternalErrorOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvInternalErrorOKButtonActionPerformed
        internalErrorCSVDialog.dispose();
    }//GEN-LAST:event_csvInternalErrorOKButtonActionPerformed

    private void pdfSuccessOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfSuccessOKButtonActionPerformed
        successPDFDialog.dispose();
    }//GEN-LAST:event_pdfSuccessOKButtonActionPerformed

    private void pdfFileNotFoundOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfFileNotFoundOKButtonActionPerformed
        fileNotFoundPDFDialog.dispose();
    }//GEN-LAST:event_pdfFileNotFoundOKButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel csvFileNotFoundErrorIcon;
    private javax.swing.JLabel csvFileNotFoundErrorMessage;
    private javax.swing.JButton csvFileNotFoundOKButton;
    private javax.swing.JLabel csvInternalErrorIcon;
    private javax.swing.JLabel csvInternalErrorMessage;
    private javax.swing.JButton csvInternalErrorOKButton;
    private javax.swing.JLabel csvSuccessDialogIcon;
    private javax.swing.JLabel csvSuccessMessage;
    private javax.swing.JButton csvSuccessOKButton;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JDialog fileNotFoundCSVDialog;
    private javax.swing.JDialog fileNotFoundPDFDialog;
    private javax.swing.JDialog internalErrorCSVDialog;
    private javax.swing.JRadioButton main911TurboWheelRadio;
    private javax.swing.JLabel mainAddUpgradesLabel;
    private javax.swing.JLabel mainBasePriceDollarLabel;
    private javax.swing.JLabel mainBasePriceTitleLabel;
    private javax.swing.JRadioButton mainBlackLeatherRadio;
    private javax.swing.JRadioButton mainBordeauxRedLeatherRadio;
    private javax.swing.JCheckBox mainBoseCheckBox;
    private javax.swing.JRadioButton mainCarreraSWheelRadio;
    private javax.swing.JRadioButton mainCarreraWhitePaintRadio;
    private javax.swing.JLabel mainCaymanDisplayLabel;
    private javax.swing.JRadioButton mainCaymanSWheelRadio;
    private javax.swing.JLabel mainCaymanTitleLabel;
    private javax.swing.JLabel mainDeliverPriceDollarLabel;
    private javax.swing.JLabel mainDeliverPriceTitleLabel;
    private javax.swing.JLabel mainEquipPriceDollarLabel;
    private javax.swing.JLabel mainEquipPriceTitleLabel;
    private javax.swing.JRadioButton mainEspressoLeatherRadio;
    private javax.swing.ButtonGroup mainExtColorButtonGroup;
    private javax.swing.JLabel mainExtColorLabel;
    private javax.swing.JRadioButton mainGuardsRedPaintRadio;
    private javax.swing.JRadioButton mainImageSelect1Radio;
    private javax.swing.JRadioButton mainImageSelect2Radio;
    private javax.swing.JRadioButton mainImageSelect3Radio;
    private javax.swing.JRadioButton mainImageSelect4Radio;
    private javax.swing.JRadioButton mainImageSelect5Radio;
    private javax.swing.JRadioButton mainImageSelect6Radio;
    private javax.swing.ButtonGroup mainImageSelectorGroup;
    private javax.swing.ButtonGroup mainIntColorButtonGroup;
    private javax.swing.JLabel mainIntColorLabel;
    private javax.swing.JRadioButton mainJetBlackPaintRadio;
    private javax.swing.JSeparator mainLeatherTransSeparator;
    private javax.swing.JRadioButton mainManualTransRadio;
    private javax.swing.JRadioButton mainMiamiBluePaintRadio;
    private javax.swing.JCheckBox mainNavCheckBox;
    private javax.swing.JCheckBox mainPASMCheckBox;
    private javax.swing.JRadioButton mainPDKTransRadio;
    private javax.swing.JSeparator mainPaintWheelSeparator;
    private javax.swing.JRadioButton mainRacingYellowPaintRadio;
    private javax.swing.JCheckBox mainSeatHeatCheckBox;
    private javax.swing.JButton mainShowOverviewButton;
    private javax.swing.JLabel mainTotalPriceDollarLabel;
    private javax.swing.JLabel mainTotalPriceTitleLabel;
    private javax.swing.ButtonGroup mainTransButtonGroup;
    private javax.swing.JLabel mainTransLabel;
    private javax.swing.JSeparator mainTransUpgradesSeparator;
    private javax.swing.JSeparator mainUpgradesOverviewSeparator;
    private javax.swing.JSeparator mainWheelLeatherSeparator;
    private javax.swing.ButtonGroup mainWheelStyleButtonGroup;
    private javax.swing.JLabel mainWheelStyleLabel;
    private javax.swing.JLabel overviewAddUpgradesNameLabel;
    private javax.swing.JLabel overviewAddUpgradesPriceLabel;
    private javax.swing.JLabel overviewAddUpgradesTitleLabel;
    private javax.swing.JLabel overviewBasePriceDollarLabel;
    private javax.swing.JLabel overviewBasePriceTitleLabel;
    private javax.swing.JLabel overviewCaymanDisplayLabel;
    private javax.swing.JLabel overviewCaymanTitleLabel;
    private javax.swing.JLabel overviewDeliverPriceDollarLabel;
    private javax.swing.JLabel overviewDeliverPriceTitleLabel;
    private javax.swing.JSeparator overviewDescriptionPaintSeparator;
    private javax.swing.JDialog overviewDialog;
    private javax.swing.JLabel overviewEquipPriceDollarLabel;
    private javax.swing.JLabel overviewEquipPriceTitleLabel;
    private javax.swing.JLabel overviewExtColorTitleLabel;
    private javax.swing.JPanel overviewGrayDescriptionPanel;
    private javax.swing.JRadioButton overviewImageSelect1Radio;
    private javax.swing.JRadioButton overviewImageSelect2Radio;
    private javax.swing.JRadioButton overviewImageSelect3Radio;
    private javax.swing.JRadioButton overviewImageSelect4Radio;
    private javax.swing.JRadioButton overviewImageSelect5Radio;
    private javax.swing.JRadioButton overviewImageSelect6Radio;
    private javax.swing.ButtonGroup overviewImageSelectorGroup;
    private javax.swing.JLabel overviewIntColorTitleLabel;
    private javax.swing.JLabel overviewLeatherColorNameLabel;
    private javax.swing.JLabel overviewLeatherColorPriceLabel;
    private javax.swing.JSeparator overviewLeatherTransSeparator;
    private javax.swing.JLabel overviewPaintColorNameLabel;
    private javax.swing.JLabel overviewPaintColorPriceLabel;
    private javax.swing.JSeparator overviewPaintWheelSeparator;
    private javax.swing.JLabel overviewTotalPriceDollarLabel;
    private javax.swing.JLabel overviewTotalPriceTitleLabel;
    private javax.swing.JLabel overviewTransNameLabel;
    private javax.swing.JLabel overviewTransPriceLabel;
    private javax.swing.JLabel overviewTransTitleLabel;
    private javax.swing.JSeparator overviewTransUpgradesSeparator;
    private javax.swing.JSeparator overviewUpgradesButtonsSeparator;
    private javax.swing.JSeparator overviewWheelLeatherSeparator;
    private javax.swing.JLabel overviewWheelStyleNameLabel;
    private javax.swing.JLabel overviewWheelStylePriceLabel;
    private javax.swing.JLabel overviewWheelStyleTitleLabel;
    private javax.swing.JLabel pdfFileNotFoundErrorIcon;
    private javax.swing.JLabel pdfFileNotFoundErrorMessage;
    private javax.swing.JButton pdfFileNotFoundOKButton;
    private javax.swing.JLabel pdfSuccessDialogIcon;
    private javax.swing.JLabel pdfSuccessMessage;
    private javax.swing.JButton pdfSuccessOKButton;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JButton saveAsCSVButton;
    private javax.swing.JButton saveAsPDFButton;
    private javax.swing.JDialog successCSVDialog;
    private javax.swing.JDialog successPDFDialog;
    // End of variables declaration//GEN-END:variables
}