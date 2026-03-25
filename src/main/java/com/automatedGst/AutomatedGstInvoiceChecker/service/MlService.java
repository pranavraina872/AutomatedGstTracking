package com.automatedGst.AutomatedGstInvoiceChecker.service;

import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.data.vector.DoubleVector;

import java.util.concurrent.ForkJoinPool;

import com.automatedGst.AutomatedGstInvoiceChecker.entity.GstTracking;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.vector.DoubleVector;
import smile.regression.LinearModel;
import smile.regression.RidgeRegression;

@Service
public class MlService {

    private LinearModel model;

    @PostConstruct
    public void init() {
        try {
            trainModel();
            System.out.println("✅ ML Model trained successfully");
        } catch (Exception e) {
            System.out.println("❌ ML Model failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void trainModel() {

        DoubleVector amount = DoubleVector.of("amount", new double[]{10000, 500000, 2000000, 15000});
        DoubleVector gstpercentage = DoubleVector.of("gstpercentage", new double[]{18, 5, 2, 18});
        DoubleVector sameState = DoubleVector.of("sameState", new double[]{1, 0, 0, 1});
        DoubleVector riskLevel = DoubleVector.of("riskLevel", new double[]{0, 1, 1, 0});

        DataFrame df = DataFrame.of(amount, gstpercentage, sameState, riskLevel);

        // ✅ FIX: define target properly
        Formula formula = Formula.lhs("riskLevel");

        model = RidgeRegression.fit(formula, df,0.01);
    }

    public String predictRisk(GstTracking gst) {

        try {
            // ✅ FIX: correct sameState logic
            int sameStateValue = gst.getBuyerStateCode().equals(gst.getSellerStateCode()) ? 1 : 0;

            // ✅ Create single-row DataFrame
            DataFrame input = DataFrame.of(
                    DoubleVector.of("amount", new double[]{gst.getAmount()}),
                    DoubleVector.of("gstpercentage", new double[]{gst.getGstTaxPercentage()}),
                    DoubleVector.of("sameState", new double[]{sameStateValue})
            );

            // ✅ FIX: use row(0) instead of casting
            double prediction = model.predict(input)[0];

            return prediction == 1 ? "HIGH" : "LOW";

        } catch (Exception e) {
            e.printStackTrace();
            return "LOW"; // fallback
        }
    }
}
