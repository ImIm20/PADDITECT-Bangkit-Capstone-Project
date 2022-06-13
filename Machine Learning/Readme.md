# PADDITECT

## 1. Load Datasets
- The project is based from Kaggle dataset. Using Machine Learning with Tensorflow as framework to classify paddy diseases, here is the link: <br> https://www.kaggle.com/competitions/paddy-disease-classification/data

## 2. Data Preprocessing
- Defining target and feature from existing CSV file:
  - 'label'=
  `['bacterial_leaf_blight' 'bacterial_leaf_streak'
 'bacterial_panicle_blight' 'blast' 'brown_spot' 'dead_heart'
 'downy_mildew' 'hispa' 'normal' 'tungro']` <br>
  - 'variety'=
  `['ADT45' 'IR20' 'KarnatakaPonni' 'Onthanel' 'Ponni' 'Surya' 'Zonal'
 'AndraPonni' 'AtchayaPonni' 'RR']`  <br>
  - 'age'= 
  `[45 50 55 60 65 68 70 72 47 77 67 73 75 80 62 66 57 82]`<br>

- Using Seaborn library for data visualization :

![__results___4_1](https://user-images.githubusercontent.com/75768911/171794388-f817c342-2e38-4022-aa3b-d9fe198f3040.png)
- Using 9367 files for training.
- Using 1040 files for validation.
- 
## 3. Deep Learning Model 96% accuracy
- Using MobilenetV2 for learning model: <br>
`preprocess_input = tf.keras.applications.mobilenet_v2.preprocess_input`<br>
- Rescaling mode:
`input_shape[500,500,3]`
- Fine tuning:<br> 
`print("Number of layers in the base model: ", len(base_model.layers))` <br>
Number of layers in the base model:  154
Fine-tune from this layer onwards
`fine_tune_at = 148`
Freeze all the layers before the layer <br>
`for layer in base_model.layers[fine_tune_at:]:
    layer.trainable = True`
- Train the model:
  -input shape : `(500,500,3)`
  - 1 Flatten layer
  - 1 Dropout layer
  - 2 BatchNormalization layers
  - 2 Dense layers
  - Output: `val_loss: 0.1457, val_accuracy: 0.9644`
- Tflite model size = 300mb

## 4. Deep Learning Model 95% validation accuracy
- Using MobilenetV2 for learning model: <br>
`preprocess_input = tf.keras.applications.mobilenet_v2.preprocess_input`<br>
- Rescaling mode:
`input_shape[500,500,3]`
- Train the model:
  -input shape : `(500,500,3)`
  - mobilenetv2
  - GlobalAveragePooling2D
  - batch normalization
  - dropout
  - dense
  - Output: `val_loss: 0.1514, val_accuracy:  0.9519`
tflite model size = 16mb

## 5. Metrics Evaluation
- based on accuracy from the 2 models, we choose the 2nd model: 95% validation accuracy since it has less complexity and less model size

## 6. Saving Model
- Saving model for Cloud Computing: <br>
`model.save('./model.h5')`
- Saving model for Android demo: <br>
`converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()`
