import requests

resp = requests.post("http://127.0.0.1:80/", files={'file': open('test3.jpg', 'rb')})

print(resp.json())