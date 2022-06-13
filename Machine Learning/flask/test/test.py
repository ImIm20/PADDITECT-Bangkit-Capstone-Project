import requests

resp = requests.post("http://34.128.124.115/", files={'file': open('test3.jpg', 'rb')})

print(resp.json())