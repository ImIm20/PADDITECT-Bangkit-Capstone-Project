import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import tensorflow as tf
import tensorflow.keras as keras
import numpy as np
import PIL.Image as Image
import io

from flask import Flask, request, jsonify

model = keras.models.load_model("model.h5", compile = False)
labels = ['tungro','hispa','downy_mildew','bacterial_leaf_streak',
 'bacterial_leaf_blight','brown_spot','blast','normal','dead_heart',
 'bacterial_panicle_blight']

def transform_image(img):
    img_arr = []
    #imgs = Image.open(img)
    imgs = np.array(img.resize((500, 500)))
    img_arr.append(imgs)
    
    return np.array(img_arr)

    # Prediction function
def predict(x):
    prediction = model.predict(transform_image(x))
    pred = np.argmax(prediction, axis=-1)
    return labels[pred[0]]

app = Flask(__name__)

@app.route("/", methods=["GET", "POST"])
def index():
    if request.method == "POST":
        file = request.files.get('file')
        if file is None or file.filename == "":
            return jsonify({"error": "no file"})

        try:
            image_bytes = file.read()
            pillow_img = Image.open(io.BytesIO(image_bytes))
            prediction = predict(pillow_img)
            data = {"prediction": (prediction)}
            return jsonify(data)
        except Exception as e:
            return jsonify({"error": str(e)})

    return "OK"


if __name__ == "__main__":
    app.run('0.0.0.0', port=80)
