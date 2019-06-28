///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package script;
//
//import DB.Parameters;
//import DB.ParametersExecution;
//import DB.Script;
//import engine.Result;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import org.apache.poi.hslf.blip.Bitmap;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfByte;
//import org.opencv.core.MatOfDMatch;
//import org.opencv.core.MatOfKeyPoint;
//import org.opencv.core.Scalar;
//import org.opencv.features2d.DMatch;
//import org.opencv.features2d.DescriptorExtractor;
//import org.opencv.features2d.DescriptorMatcher;
//import org.opencv.features2d.FeatureDetector;
//import org.opencv.features2d.Features2d;
//import org.opencv.highgui.Highgui;
//import org.opencv.imgproc.Imgproc;
//
///**
// *
// * @author T0155040
// */
//public class compareImages implements InterfaceScript {
//
//    private String image1, image2, savePath;
//    private double seuil;
//
////    @Override
////    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
////        DecimalFormat df = new DecimalFormat("###.#####");
//////
////        System.load("C:\\opencv-2\\build\\java\\x64\\opencv_java2411.dll");
//////
////        image1 = parameters.get(1).getValue();
////        image2 = parameters.get(2).getValue();
////        seuil = Double.valueOf(parameters.get(3).getValue());
//////
//////        double accuracy = 0;
//////
//////        int match_method = Imgproc.TM_CCOEFF;
//////
////        Mat templ = Highgui.imread(image1);
////
////        Mat img = Highgui.imread(image2);
//////
//////        int result_cols = img.cols() - templ.cols() + 1;
//////        int result_rows = img.rows() - templ.rows() + 1;
//////        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
//////
//////        Imgproc.matchTemplate(img, templ, result, match_method);
//////        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
//////        CommonFunctions.debugLog.error("MMR max value : " + mmr.maxVal);
//////        CommonFunctions.debugLog.error("MMR max value : " + mmr.minVal);
//////        accuracy = mmr.maxVal;
//////
//////        if (mmr.maxVal > (seuil * 100000)) {
//////            Point point = mmr.maxLoc;
//////            Imgproc.rectangle(img, point, new Point(point.x + templ.cols(), point.y + templ.rows()), new Scalar(0, 255, 0));
//////        }
////
////        //Step 1: Detect and drawKeyPoints
////        MatOfKeyPoint keyPointsSource = new MatOfKeyPoint(), keyPointsSubImg = new MatOfKeyPoint();
////        FeatureDetector fd = FeatureDetector.create(FeatureDetector.SURF);
////        fd.detect(img, keyPointsSource);
////        fd.detect(templ, keyPointsSubImg);
////
//////Step 2: Set the descriptor corresponding to the keyPoints Matrix
////        Mat descrMatSource = new Mat(), descrMatSubImg = new Mat();
////        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
////        extractor.compute(img, keyPointsSource, descrMatSource);
////        extractor.compute(templ, keyPointsSubImg, descrMatSubImg);
////
////        //Step 3: compare the two descriptors generated above, Method : BRUTEFORCE more accurate but much longer
////        MatOfDMatch matches = new MatOfDMatch();
////        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
////        matcher.match(descrMatSubImg, descrMatSource, matches);
////
//////Step 4: filter what're good matches based on the minimal distance comparison
////        double max_dist = 0;
////        double min_dist = 100;
////
////        for (DMatch oneMatch : matches.toArray()) {
////
////            if (oneMatch.distance < min_dist) {
////                min_dist = oneMatch.distance;
////            }
////            if (oneMatch.distance > max_dist) {
////                max_dist = oneMatch.distance;
////            }
////        }
////
////        ArrayList<DMatch> goodMatch = new ArrayList<>();
////
////        for (DMatch oneMatch : matches.toArray()) {
////
////            if (oneMatch.distance < min_dist * 1.5) {
////                goodMatch.add(oneMatch);
////            }
////        }
////
////        CommonFunctions.debugLog.error(goodMatch.size() + " good matches found ");
////        CommonFunctions.debugLog.error("maxDist : " + max_dist);
////        CommonFunctions.debugLog.error("minDist : " + min_dist);
////
//////Drawing good matches
////        Mat img_matches = new Mat();
////        MatOfDMatch MatGoodMatches = new MatOfDMatch();
////        MatGoodMatches.fromList(goodMatch);
////        Features2d.drawMatches(templ, keyPointsSubImg, img, keyPointsSource, MatGoodMatches, img_matches, Scalar.all(-1), Scalar.all(-1), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
////        Highgui.imwrite("D:\\Users\\t0155040\\Desktop\\plop12.png", img_matches);
////        //Highgui.imwrite("D:\\Users\\t0155040\\Desktop\\plop12.png", img);
////
////
////        Result returnResult = new Result();
//////        returnResult.setComment("Matching value : " + df.format(goodMatch.size()));
//////        if (goodMatch.size() >= (seuil * 1000)) {
//////            returnResult.setResult("OK");
//////        } else {
//////            returnResult.setResult("NOK");
//////        }
////        return returnResult;
////
////   }
//    @Override
//    public void close() {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public ArrayList<Parameters> parameters() {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        return null;
//    }
//
//    @Override
//    public Script scriptInfos() {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        return null;
//    }
//
//    @Override
//    public int paramNumber() {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        return 0;
//    }
//
//    @Override
//    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
//        DecimalFormat df = new DecimalFormat("###.#####");
////
//        System.load("C:\\opencv-2\\build\\java\\x64\\opencv_java2411.dll");
////
//        image1 = parameters.get(1).getValue();
//        image2 = parameters.get(2).getValue();
//        seuil = Double.valueOf(parameters.get(3).getValue());
//
//        double minimum = (seuil*20)/100;
//        Mat image01 = Highgui.imread(image1);//template
//
//        Mat image02 = Highgui.imread(image2);//to look
//        if (image01 == null || image02 == null) {
//            CommonFunctions.debugLog.error("At least one of the image is null");
//            System.exit(0);
//        }
//        Mat grayImage01 = new Mat(image01.rows(), image01.cols(), image01.type());
//        Imgproc.cvtColor(image01, grayImage01, Imgproc.COLOR_BGRA2GRAY);
//        Core.normalize(grayImage01, grayImage01, 0, 255, Core.NORM_MINMAX);
//        Mat grayImage02 = new Mat(image02.rows(), image02.cols(), image02.type());
//        Imgproc.cvtColor(image02, grayImage02, Imgproc.COLOR_BGRA2GRAY);
//        Core.normalize(grayImage02, grayImage02, 0, 255, Core.NORM_MINMAX);
////        FeatureDetector siftDetector = FeatureDetector.create(FeatureDetector.SIFT);
////        DescriptorExtractor siftExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
////        MatOfKeyPoint keyPoint01 = new MatOfKeyPoint();
////        siftDetector.detect(grayImage01, keyPoint01);
////        MatOfKeyPoint keyPoint02 = new MatOfKeyPoint();
////        siftDetector.detect(grayImage02, keyPoint02);
////        Mat descripters01 = new Mat(image01.rows(), image01.cols(), image01.type());
////        siftExtractor.compute(grayImage01, keyPoint01, descripters01);
////        Mat descripters02 = new Mat(image02.rows(), image02.cols(), image02.type());
////        siftExtractor.compute(grayImage02, keyPoint02, descripters02);
////        MatOfDMatch matchs = new MatOfDMatch();
////        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
////        matcher.match(descripters01, descripters02, matchs);
////        int N = 50;
////        DMatch[] tmp01 = matchs.toArray();
////        CommonFunctions.debugLog.error("temp01 size" + tmp01.length);
//////        DMatch[] tmp02 = new DMatch[N];
//////        for (int i = 0; i < tmp02.length; i++) {
//////            tmp02[i] = tmp01[i];
//////        }
////        matchs.fromArray(tmp01);
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        int minute = Calendar.getInstance().get(Calendar.MINUTE);
//        int second = Calendar.getInstance().get(Calendar.SECOND);
//        String now = year + "" + month + "" + day + "" + hour + "" + minute + "" + second;
////        Mat matchedImage = new Mat(image01.rows(), image01.cols() * 2, image01.type());
////        Features2d.drawMatches(image01, keyPoint01, image02, keyPoint02, matchs, matchedImage);
////// 出力画像 at SIFT
////        Highgui.imwrite("D:\\Users\\t0155040\\Desktop\\SIFT" + now + ".png", matchedImage);
//        
//        
//        FeatureDetector surfDetector = FeatureDetector.create(FeatureDetector.SURF);
//        DescriptorExtractor surfExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
//        MatOfKeyPoint keyPoint01 = new MatOfKeyPoint();
//        surfDetector.detect(grayImage01, keyPoint01);
//        MatOfKeyPoint keyPoint02 = new MatOfKeyPoint();
//        surfDetector.detect(grayImage02, keyPoint02);
//        Mat descripters01 = new Mat(image01.rows(), image01.cols(), image01.type());
//        surfExtractor.compute(grayImage01, keyPoint01, descripters01);
//        Mat descripters02 = new Mat(image02.rows(), image02.cols(), image02.type());
//        surfExtractor.compute(grayImage02, keyPoint02, descripters02);
//        MatOfDMatch matchs = new MatOfDMatch();
//        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
//        matcher.match(descripters01, descripters02, matchs);
//        DMatch[] tmp03 = matchs.toArray();
////        DMatch[] tmp04 = new DMatch[50];
////        for (int i = 0; i < tmp04.length; i++) {
////            tmp04[i] = tmp03[i];
////        }
//        matchs.fromArray(tmp03);
//        Mat matchedImage = new Mat(image01.rows(), image01.cols() * 2, image01.type());
//        Features2d.drawMatches(image01, keyPoint01, image02, keyPoint02, matchs, matchedImage);
//// 出力画像 at SURF
//        Highgui.imwrite("D:\\Users\\t0155040\\Desktop\\SURF" + now + ".png", matchedImage);
//        CommonFunctions.debugLog.error("Number of Matches SURF : " + tmp03.length );
//        Result returnResult = new Result();
//        returnResult.setComment("Matching value : " + df.format(tmp03.length/20));
//        if (tmp03.length >= (minimum)) {
//            returnResult.setResult("OK");
//        } else {
//            returnResult.setResult("NOK");
//        }
//        return returnResult;
//    }
//
//}
