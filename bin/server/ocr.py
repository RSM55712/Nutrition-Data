import io
import re
from google.cloud import vision

client = vision.ImageAnnotatorClient
client = client.from_service_account_json("cloud_credentials.json")


def analyze_image(path):
    with io.open(path, 'rb') as image_file:
        content = image_file.read()

    image = vision.types.Image(content=content)

    response = client.text_detection(image=image)
    texts = response.text_annotations

    return texts


def detect_text(texts):
    image_text = ""

    for text in texts:
        image_text += "\n" + text.description
    return image_text

def find_Calories(texts):
    location = texts.find("Calories") + 9
    second_space = texts.find(" ",location)
    calories = (texts[location: second_space - 1])
    if calories == "O":
        calories = 0
    calories = float(calories)

    return calories


def find_TotalFat(texts):
    sample_text = ""
    location = texts.find("Fat") + 4
    secondSpace = texts.find("g",location)
    fat = (texts[location: secondSpace])
    if fat == "O":
        fat = 0
    fat = float(fat)
    return fat

def find_TransFat(texts):
    sample_text = ""
    location = texts.find("Trans") + 10
    secondSpace = texts.find("g",location)
    trans_fat = (texts[location: secondSpace])
    if trans_fat == "O":
        trans_fat = 0
    trans_fat = float(trans_fat)
    return trans_fat

def find_Cholesterol(texts):
    sample_text = ""
    location = texts.find("Cholesterol") + 12
    secondSpace = texts.find("m",location)
    chol = (texts[location: secondSpace])
    if chol == "O":
        chol = 0
    chol = float(chol)
    return chol

def find_Added_Sugars(texts):
    sample_text = ""
    location = texts.find("Includes") + 9
    secondSpace = texts.find("g",location)
    sug = (texts[location: secondSpace])
    if sug == "O":
        sug = 0
    sug = float(sug)
    return sug

def find_Protein(texts):
    sample_text = ""
    location = texts.find("Protein") + 8
    secondSpace = texts.find("g",location)
    pro = (texts[location: secondSpace])
    if pro == "O":
        pro = 0
    pro = float(pro)
    return pro

def find_BMI(weight, height):
   bmi = float((weight * 0.45)/(math.pow((height * 0.025), 2)))
    return bmi

if __name__ == "__main__":
    ##print(detect_text(analyze_image("image.jpg")))
    print(find_Calories(detect_text(analyze_image("image.jpg"))))
    print(find_TotalFat(detect_text(analyze_image("image.jpg"))))
    print(find_TransFat(detect_text(analyze_image("image.jpg"))))
    print(find_Cholesterol(detect_text(analyze_image("image.jpg"))))
    print(find_Added_Sugars(detect_text(analyze_image("image.jpg"))))
    print(find_Protein(detect_text(analyze_image("image.jpg"))))




