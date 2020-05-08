package Printer;

import Controller.Controller;
import View.AbstractRunwayView;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.sql.Time;
import java.time.LocalTime;

public class ViewPrinter {

    public static void printResult(Node node, Stage owner)
    {
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null)
        {
            return;
        }

        boolean proceed = job.showPrintDialog(owner);

        if (proceed)
        {
            boolean printed = job.printPage(node);
            if (printed)
            {
                job.endJob();
                Controller.notificationsString.append("Printer: Successfully printed results").append(" (").append(Time.valueOf(LocalTime.now())).append(")").append("\n");
                synchronized (Controller.notificationsString)
                {
                    Controller.notificationsString.notify();
                }
            }



        }
    }

    public static void printRunway(AbstractRunwayView runway, Stage owner)
    {
        WritableImage writableImage = new WritableImage((int)runway.getWidth() ,
                (int)runway.getHeight());
        runway.snapshot(null, writableImage);
        ImageView imageView = new ImageView(writableImage);
        javafx.print.Printer printer = javafx.print.Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, javafx.print.Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / imageView.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / imageView.getBoundsInParent().getHeight();
        imageView.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean successPrintDialog = job.showPrintDialog(owner);
            if(successPrintDialog){
                boolean success = job.printPage(pageLayout,imageView);
                if (success) {
                    job.endJob();
                    Controller.notificationsString.append("Printer: Successfully printed runway").append(" (").append(Time.valueOf(LocalTime.now())).append(")").append("\n");
                    synchronized (Controller.notificationsString)
                    {
                        Controller.notificationsString.notify();
                    }
                }
            }
        }
    }
}
