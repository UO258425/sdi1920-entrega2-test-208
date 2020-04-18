import json
import requests
from urllib3.exceptions import InsecureRequestWarning
import unittest

class TestApiRequests(unittest.TestCase):

    def setUp(self):
        requests.packages.urllib3.disable_warnings()
        requests.packages.urllib3.disable_warnings(requests.packages.urllib3.exceptions.InsecureRequestWarning)


    def loginAs(email, password):
        r = requests.post("https://localhost:8081/api/autenticar", json={"email":email, "password":password}, verify=False)
        return (r.json(),r.status_code)
        
    #
    # Identificarse en la api con datos validos
    #
    def test_PR33(self):
        json, status = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")
        self.assertEqual(200, status)
        self.assertTrue(json['token'])

    #
    # Identificarse en la api con datos invalidos
    #
    def test_PR34(self):
        json, status = TestApiRequests.loginAs("prueba1@prueba1", "asdfasdf")
        self.assertEqual(401, status)
        self.assertEqual( False, json['autenticado'])


    #
    # Listar todos los amigos
    #
    def test_PR35(self):
        json, status = TestApiRequests.loginAs("prueba2@prueba2", "prueba2")
        r2 = requests.get("https://localhost:8081/api/amigos", headers={"token":json['token']}, verify=False)
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
        json, status = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")

        r2 = requests.post("https://localhost:8081/api/mensajes", headers={"token":json['token']}, json={"to":"prueba2@prueba2", "text":"mensaje de prueba desde python"}, verify=False)

        self.assertEqual(201, r2.status_code)
        self.assertEqual( "Mensaje enviado", r2.json()['message'])


    #
    # Crear mensaje a un usuario que no es tu amigo
    #
    def test_PR38(self):
        json, status = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")

        r2 = requests.post("https://localhost:8081/api/mensajes", headers={"token":json['token']}, json={"to":"prueba10@prueba10", "text":"mensaje de prueba desde python"}, verify=False)

        self.assertEqual(400, r2.status_code)
        self.assertEqual( "El destinatario no es tu amigo", r2.json()['message'])


    #
    # Obtener mensajes conversación
    #
    def test_PR39(self):
        json1, status1 = TestApiRequests.loginAs("prueba2@prueba2", "prueba2")


        json2, status2 = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")

        
        r3 = requests.get("https://localhost:8081/api/mensajes", headers={"token":json1['token'], "user1": json1['yourID'], "user2":json2['yourID']}, verify=False)
        self.assertEqual(200, r3.status_code)
        self.assertTrue(len(r3.json())>0)

    #
    # Obtener mensajes de una conversacion a la que no perteneces
    #
    def test_PR40(self):
        json1, status1 = TestApiRequests.loginAs("prueba2@prueba2", "prueba2")
        json2, status2 = TestApiRequests.loginAs("prueba11@prueba11", "prueba11")
        json3, status3 = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")
        
        r4 = requests.get("https://localhost:8081/api/mensajes", headers={"token":json1['token'], "user1": json2['yourID'], "user2":json3['yourID']}, verify=False)
        self.assertEqual(400, r4.status_code)
        self.assertEqual( "No formas parte de la conversacion", r4.json()['message'])


    #
    # Marcar como leido un mensaje
    #
    def test_PR41(self):
        json1, status1 = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")
        json2, status2 = TestApiRequests.loginAs("prueba2@prueba2", "prueba2")
        
        r3 = requests.get("https://localhost:8081/api/mensajes", headers={"token":json1['token'], "user1": json1['yourID'], "user2":json2['yourID']}, verify=False)
        mensajes = r3.json()
  
        r4 = requests.put("https://localhost:8081/api/mensajes", headers={"token":json1['token']}, json={"messageId":mensajes[0]['_id']}, verify=False)
        self.assertEqual(204, r4.status_code)

    #
    # Marcar como leido un mensaje con id incorrecto
    #
    def test_PR42(self):
        json1, status1 = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")
  
        r2 = requests.put("https://localhost:8081/api/mensajes", headers={"token":json1['token']}, json={"messageId":"111"}, verify=False)
        self.assertEqual(400, r2.status_code)

    #
    # Marcar como leido un mensaje con id que no existe
    #
    def test_PR43(self):
        json1, status1 = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")
  
        r2 = requests.put("https://localhost:8081/api/mensajes", headers={"token":json1['token']}, json={"messageId":"123456789123456789123456"}, verify=False)
        self.assertEqual(404, r2.status_code)
        self.assertEqual( "El mensaje no existe", r2.json()['message'])

    #
    # Marcar como leido un mensaje que no pertenece a tus conversaciones
    #
    def test_PR44(self):
        json1, status1 = TestApiRequests.loginAs("prueba1@prueba1", "prueba1")
        json2, status2 = TestApiRequests.loginAs("prueba2@prueba2", "prueba2")

        r3 = requests.get("https://localhost:8081/api/mensajes", headers={"token":json1['token'], "user1":  json1['yourID'], "user2":json2['yourID']}, verify=False)
        mensajes = r3.json()

        json3, status3 = TestApiRequests.loginAs("prueba3@prueba3", "prueba3")
  
        r5 = requests.put("https://localhost:8081/api/mensajes", headers={"token":json3['token']}, json={"messageId":mensajes[0]['_id']}, verify=False)
        self.assertEqual(401, r5.status_code)
        self.assertEqual( "El mensaje no pertenece a ninguna de tus conversaciones", r5.json()['message'])


if __name__ == '__main__':  
    unittest.main()

