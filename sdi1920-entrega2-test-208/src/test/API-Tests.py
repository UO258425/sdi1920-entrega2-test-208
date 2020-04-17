import json

import requests
from urllib3.exceptions import InsecureRequestWarning
import unittest

class TestApiRequests(unittest.TestCase):

    def setUp(self):
        requests.packages.urllib3.disable_warnings()
        requests.packages.urllib3.disable_warnings(requests.packages.urllib3.exceptions.InsecureRequestWarning)
        
    #
    # Identificarse en la api con datos validos
    #
    def test_PR33(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        self.assertEqual(200, r.status_code)
        self.assertTrue("token" in r.json())

    #
    # Identificarse en la api con datos invalidos
    #
    def test_PR34(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"estanoesmipassword"}, verify=False)
        self.assertEqual(401, r.status_code)
        self.assertEqual( False, r.json()['autenticado'])


    #
    # Listar todos los amigos
    #
    def test_PR35(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba2@prueba2", "password":"prueba2"}, verify=False)
        token = r.json()['token']
        r2 = requests.get("https://localhost:8081/api/amigos", headers={"token":token}, verify=False)
        self.assertEqual(200, r2.status_code)
        self.assertEqual(1, len(r2.json()))


    #
    # Intento de uso de la api sin autenticacion
    #
    def test_PR36(self):
        r = requests.get("https://localhost:8081/api/amigos", verify=False)
        self.assertEqual(403, r.status_code)
        self.assertEqual(False, r.json()['acceso'])

    #
    # Crear un mensaje valido
    #
    def test_PR37(self):        
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        token = r.json()['token']

        r2 = requests.post("https://localhost:8081/api/mensajes", headers={"token":token}, json={"to":"prueba2@prueba2", "text":"mensaje de prueba desde python"}, verify=False)

        self.assertEqual(201, r2.status_code)
        self.assertEqual( "Mensaje enviado", r2.json()['message'])


    #
    # Crear mensaje a un usuario que no es tu amigo
    #
    def test_PR38(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        token = r.json()['token']

        r2 = requests.post("https://localhost:8081/api/mensajes", headers={"token":token}, json={"to":"prueba10@prueba10", "text":"mensaje de prueba desde python"}, verify=False)

        self.assertEqual(400, r2.status_code)
        self.assertEqual( "El destinatario no es tu amigo", r2.json()['message'])


    #
    # Obtener mensajes conversación
    #
    def test_PR39(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba2@prueba2", "password":"prueba2"}, verify=False)
        token = r.json()['token']
        user1 = r.json()['yourID']

        r2 = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        user2 = r2.json()['yourID']
        
        r3 = requests.get("https://localhost:8081/api/mensajes", headers={"token":token, "user1": user1, "user2":user2}, verify=False)
        self.assertEqual(200, r3.status_code)
        self.assertTrue(len(r3.json())>0)

    #
    # Obtener mensajes de una conversacion a la que no perteneces
    #
    def test_PR40(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba2@prueba2", "password":"prueba2"}, verify=False)
        token = r.json()['token']
  
        r2 = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba11@prueba11", "password":"prueba11"}, verify=False)
        user1 = r2.json()['yourID']

        r3 = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        user2 = r3.json()['yourID']
        
        r4 = requests.get("https://localhost:8081/api/mensajes", headers={"token":token, "user1": user1, "user2":user2}, verify=False)
        self.assertEqual(400, r4.status_code)
        self.assertEqual( "No formas parte de la conversacion", r4.json()['message'])


    #
    # Marcar como leido un mensaje
    #
    def test_PR41(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        token = r.json()['token']
        user1 = r.json()['yourID']
  
        r2 = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba2@prueba2", "password":"prueba2"}, verify=False)
        user2 = r2.json()['yourID']

        r3 = requests.get("https://localhost:8081/api/mensajes", headers={"token":token, "user1": user1, "user2":user2}, verify=False)
        mensajes = r3.json()
  
        r4 = requests.put("https://localhost:8081/api/mensajes", headers={"token":token}, json={"messageId":mensajes[0]['_id']}, verify=False)
        self.assertEqual(204, r4.status_code)

    #
    # Marcar como leido un mensaje con id incorrecto
    #
    def test_PR42(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        token = r.json()['token']
  
        r2 = requests.put("https://localhost:8081/api/mensajes", headers={"token":token}, json={"messageId":"111"}, verify=False)
        self.assertEqual(400, r2.status_code)

    #
    # Marcar como leido un mensaje con id que no existe
    #
    def test_PR43(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        token = r.json()['token']
  
        r2 = requests.put("https://localhost:8081/api/mensajes", headers={"token":token}, json={"messageId":"123456789123456789123456"}, verify=False)
        self.assertEqual(404, r2.status_code)
        self.assertEqual( "El mensaje no existe", r2.json()['message'])

    #
    # Marcar como leido un mensaje que no pertenece a tus conversaciones
    #
    def test_PR44(self):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba1@prueba1", "password":"prueba1"}, verify=False)
        token = r.json()['token']
        user1 = r.json()['yourID']
  
        r2 = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba2@prueba2", "password":"prueba2"}, verify=False)
        user2 = r2.json()['yourID']

        r3 = requests.get("https://localhost:8081/api/mensajes", headers={"token":token, "user1": user1, "user2":user2}, verify=False)
        mensajes = r3.json()

        r4 = requests.post("https://localhost:8081/api/autenticar", json={"email":"prueba3@prueba3", "password":"prueba3"}, verify=False)
        token = r4.json()['token']
  
        r5 = requests.put("https://localhost:8081/api/mensajes", headers={"token":token}, json={"messageId":mensajes[0]['_id']}, verify=False)
        self.assertEqual(401, r5.status_code)
        self.assertEqual( "El mensaje no pertenece a ninguna de tus conversaciones", r5.json()['message'])


if __name__ == '__main__':  
    unittest.main()

