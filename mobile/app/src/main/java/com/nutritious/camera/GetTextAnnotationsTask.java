package com.nutritious.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

class GetTextAnnotationsTask extends AsyncTask<GetTextAnnotationsTaskParams, Void, TextAnnotation> {

    private Exception exception;
    private GetTextAnnotationsTaskParams params;
    private Bitmap compressedImage;

    protected TextAnnotation doInBackground(GetTextAnnotationsTaskParams... taskParams) {
        this.params = taskParams[0];

        Vision.Builder visionBuilder = new Vision.Builder(
            new NetHttpTransport(),
            new AndroidJsonFactory(),
            null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyCmdmyGkn2DOdW0AM0Jyju57crT33z3UEw"));

        Vision vision = visionBuilder.build();

        try {
            BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                    new BatchAnnotateImagesRequest();
            batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                // Add the image
                Image base64EncodedImage = new Image();
                // Convert the bitmap to a JPEG
                // Just in case it's a format that Android understands but Cloud Vision
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap.createScaledBitmap(params.bitmap, (int) (params.bitmap.getWidth() / 2), (int) (params.bitmap.getHeight() / 2), false).compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                compressedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                // Base64 encode the JPEG
                base64EncodedImage.encodeContent(imageBytes);
                annotateImageRequest.setImage(base64EncodedImage);

                // add the features we want
                annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                    Feature labelDetection = new Feature();
                    labelDetection.setType("DOCUMENT_TEXT_DETECTION");
                    add(labelDetection);
                }});

                // Add the list of one thing to the request
                add(annotateImageRequest);
            }});

            // Due to a bug: requests to Vision API containing large images fail when GZipped.
            Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
            annotateRequest.setDisableGZipContent(true);

            /*Image base64enco = new Image();
            inputImage.encodeContent(params.data);

            Feature desiredFeature = new Feature();
            desiredFeature.setType("TEXT_DETECTION");

            AnnotateImageRequest request = new AnnotateImageRequest();
            request.setImage(inputImage);
            request.setFeatures(Arrays.asList(desiredFeature));

            BatchAnnotateImagesRequest batchRequest =
                    new BatchAnnotateImagesRequest();

            batchRequest.setRequests(Arrays.asList(request));*/

            BatchAnnotateImagesResponse batchResponse = annotateRequest.execute();

            Log.i("Suraj", batchResponse.getResponses().toString());
            Log.i("Suraj2", batchResponse.getResponses().get(0).getFullTextAnnotation().toString());
            return batchResponse.getResponses()
                    .get(0).getFullTextAnnotation();
        } catch (Exception e) {
            this.exception = e;
            Log.i("Rohit", e.toString());
            return null;
        }
    }

    protected void onPostExecute(TextAnnotation text) {
        params.main.result.setImageBitmap(compressedImage);
        OCR ocr = new OCR();
        String finalText = "";
        if (text != null) {
            User user = new SQLiteDatabaseHandler(params.main).getUser();
            String spaceText = text.getText().toString().replaceAll("\n", " ");
            try {
                finalText += ocr.calorieResponse(params.main.mealType, user.getWeight(), user.getHeight(), user.getSex(), user.getAge(), spaceText) + "\n";
            } catch (Exception e) { }
            try {
                if (text.getText().contains("Includes"))
                    finalText += ocr.addedSugarResponse(params.main.mealType, user.getWeight(), user.getHeight(), user.getSex(), user.getAge(), spaceText) + "\n";
            } catch (Exception e) { }
            try {
                finalText += ocr.cholesterolResponse(params.main.mealType, user.getWeight(), user.getHeight(), user.getSex(), user.getAge(), spaceText) + "\n";
            } catch (Exception e) { }
            try {
                finalText += ocr.totalFatResponse(params.main.mealType, user.getWeight(), user.getHeight(), user.getSex(), user.getAge(), spaceText) + "\n";
            } catch (Exception e) { }
            try {
                finalText += ocr.saturatedFatResponse(params.main.mealType, user.getWeight(), user.getHeight(), user.getSex(), user.getAge(), spaceText) + "\n";
            } catch (Exception e) { }
            try {
                finalText += ocr.transFatResponse(params.main.mealType, user.getWeight(), user.getHeight(), user.getSex(), user.getAge(), spaceText) + "\n";
            } catch (Exception e) { }
            try {
                finalText += ocr.protein(params.main.mealType, user.getWeight(), user.getHeight(), user.getSex(), user.getAge(), spaceText) + "\n";
            } catch (Exception e) { }
        }
        params.main.resultText.setText(text != null ? finalText : "Could not identify Label.");
    }
}

class GetTextAnnotationsTaskParams {
    public Bitmap bitmap;
    public MainActivity main;

    public GetTextAnnotationsTaskParams(MainActivity main, Bitmap data) {
        this.main = main;
        this.bitmap = data;
    }
}


