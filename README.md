Server for the coffriend apps. Learning project.

# Documentation
## AUTH / LOGIN
### POST /api/usuaris
Descripció: Crea un nou usuari. El primer usuari creat serà l'administrador. 

Els usuaris posteriors seran clients per defecte.

Per crear un usuari amb rol treballador s'ha de especificar afegint com a rol "staff" (solament pot crear un usuari staff un usuari administrador).

> [!NOTE]
> ADMIN:
> L'instancia publicada ja té usuari admin amb contrasenya "1234" i email "admin@example.com".

```json
Request:
{
  "nom": "Anna",
  "email": "anna@example.com",
  "password": "1234",
  "rol": "staff", // optional: "admin", "staff" o "client"
  "idBotiga": 1
}

Response:
{
    "id": 70,
    "nom": "Anna",
    "email": "anna@example.com",
    "rol": "staff",
    "nivell": null,
    "punts": null,
    "idBotiga": 1,
    "insignies": []
}
```

### POST /api/auth/login
```json
Request:
{
  "email": "testclient@example.com",
  "password": "1234"
}

Response:
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiY2xpZW50Iiwic3ViIjoiNzUiLCJpYXQiOjE3NzY5MDQ0MTEsImV4cCI6MTc3Njk5MDgxMX0.8IlyCbBjqttMoNKMp2HZawEa-MOfKe-L08K1lZaZU-w",
    "user": {
        "id": 75,
        "nom": "Test Client",
        "email": "testclient+1776904390328@example.com",
        "rol": "client",
        "nivell": 1,
        "punts": 0,
        "idBotiga": null,
        "insignies": null
    }
}
```

### POST /api/auth/logout
```json
Request:
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJzdWIiOiI4MCIsImlhdCI6MTc3NjkwNTk5NywiZXhwIjoxNzc2OTkyMzk3fQ.w9jP1V5Fvia9cmPi8UO_JrbeFT-sJOB9MlqrhyXjRxs"
}

Response:
{
    "success": true,
    "message": "Logout successful"
}
```

### GET /api/usuaris
Descripció:
- Si l'usuari que la demana es administrador retorna un llistat complet.
- Si l'usuari que la demana es staff es retorna un llistat amb un perfil limitat dels usuaris de la botiga en la que treballa. 
- Si l'usuari es client es retorna error 403.
>  Usuaris sense botiga no seran visibles al treballador. Si no s'ha assignat botiga al treballador no podrá veure ningun usuari.

```json 
Header:
  Authorization: Bearer [token]
```

```json 
Response (admin):
[
  {
    "id": 1,
    "nom": "admin",
    "email": "admin@example.com",
    "rol": "admin",
    "nivell": null,
    "punts": null,
    "idBotiga": null,
    "insignies": []
  },
  {
    "id": 7,
    "nom": "New Staff",
    "email": "newstaff+1776895308470@example.com",
    "rol": "staff",
    "nivell": null,
    "punts": null,
    "idBotiga": null,
    "insignies": []
  },
  {
    "id": 18,
    "nom": "Test Client",
    "email": "testclient+1776897524632@example.com",
    "rol": "client",
    "nivell": 1,
    "punts": 0,
    "idBotiga": null,
    "insignies": []
  },
  {
    "id": 23,
    "nom": "Anna",
    "email": "anna@example.com",
    "rol": "client",
    "nivell": 1,
    "punts": 100,
    "idBotiga": null,
    "insignies": [
      {
        "id": 19,
        "nom": "Gran Despesa",
        "descripcio": "Gasta 50€ o més en una sola comanda",
        "imatgeUrl": null,
        "dataObtencio": "2026-04-22"
      }
    ]
  },

]

Response (staff botiga 1): // TODO
[
  {
    "id": 18,
    "nom": "Test Client",
    "email": "testclient+1776897524632@example.com",
    "rol": "client",
    "nivell": 1,
    "punts": 0,
    "idBotiga": 1,
    "insignies": []
  },
  
  {
    "id": 23,
    "nom": "Anna",
    "email": "anna@example.com",
    "rol": "client",
    "nivell": 1,
    "punts": 100,
    "idBotiga": 1,
    "insignies": [
      {
        "id": 19,
        "nom": "Gran Despesa",
        "descripcio": "Gasta 50€ o més en una sola comanda",
        "imatgeUrl": null,
        "dataObtencio": "2026-04-22"
      }
    ]
  }

]
```

### GET /api/usuaris/[id]
``` json
Header:
  Authorization: Bearer [token]

Response:
{
    "id": 73,
    "nom": "Pedro",
    "email": "pedro@example.com",
    "rol": "client",
    "nivell": 2,
    "punts": 250,
    "idBotiga": null,
    "insignies": [
        {
            "id": 61,
            "nom": "Matiner",
            "descripcio": "Demana cafè entre les 6h i les 8h",
            "imatgeUrl": null,
            "dataObtencio": "2026-04-23"
        },
        {
            "id": 62,
            "nom": "Cafè espress",
            "descripcio": "Compra cafes espresso",
            "imatgeUrl": null,
            "dataObtencio": "2026-04-23"
        }
    ]
}
```

### PUT /api/usuaris/[id]
Descripció: Permet actualitzar un usuari per el seu id. 
Pot actualitzar les dades del compte l'usuari i l'admin.

```json 
Header:
  Authorization: Bearer [token]

Request:
{
    "nom": "Pedro Updated",
    "email": "pedro@example.com"
}

Response:
{
    "id": 73,
    "nom": "Pedro Updated",
    "email": "pedro@example.com",
    "rol": "client",
    "nivell": null,
    "punts": null,
    "idBotiga": null,
    "insignies": [
        {
            "id": 61,
            "nom": "Matiner",
            "descripcio": "Demana cafè entre les 6h i les 8h",
            "imatgeUrl": null,
            "dataObtencio": "2026-04-23"
        },
        {
            "id": 62,
            "nom": "Cafè espress",
            "descripcio": "Compra cafes espresso",
            "imatgeUrl": null,
            "dataObtencio": "2026-04-23"
        }
    ]
}
```

### DELETE /api/usuaris/[id]
Descripció: esborra l'usuari amb el seu id.

```json
Header:
  Authorization: Bearer [token]
```


## Comandes
### POST /api/comandes

```json
Header:
  Authorization: Bearer [token]

Request:
{
    "idBotiga": 24,
    "tipus": "local",
    "linies": [
        {
            "idProducte": 196,
            "quantitat": 1,
            "preuUnitari": 2.5
        }
    ]
}

Response:
{
    "id": 19,
    "dataHora": "2026-04-23T01:00:03.418744857",
    "estat": "pendent",
    "tipus": "local",
    "idUsuari": 83,
    "idBotiga": 24,
    "linies": [
        {
            "idComanda": 19,
            "idProducte": 196,
            "quantitat": 1,
            "preuUnitari": 2.5
        }
    ]
}
```

### GET /api/comandes/[id]
- El client pot obtenir la comanda si es la seva
- El treballador si la comanda te el mateix id botiga
- L'admin pot obtenir qualsevol comanda.

```json
Header:
  Authorization: Bearer [token]

Response:
{
  "id": 19,
  "dataHora": "2026-04-23T01:00:03.418744857",
  "estat": "pendent",
  "tipus": "local",
  "idUsuari": 83,
  "idBotiga": 24,
  "linies": [
      {
          "idComanda": 19,
          "idProducte": 196,
          "quantitat": 1,
          "preuUnitari": 2.5
      }
  ]
} 
```

### GET /api/comandes
- El treballador obté totes les comandes que tenen el mateix id botiga.
- L'admin obté totes les comandes.
- El client no té acces a aquest llistat.

```json
Header:
  Authorization: Bearer [token]

Response:
[
  {
    "id": 19,
    "dataHora": "2026-04-23T01:00:03.418744857",
    "estat": "pendent",
    "tipus": "local",
    "idUsuari": 83,
    "idBotiga": 24,
    "linies": [
        {
            "idComanda": 19,
            "idProducte": 196,
            "quantitat": 1,
            "preuUnitari": 2.5
        }
    ]
  } 
]
```

### GET /api/comandes/usuario/[clientId]
- El client pot obtenir les seves comandes
- El treballador les que tenen el mateix id botiga
- L'admin pot obtenir qualsevol comanda.

```json
Header:
  Authorization: Bearer [token]

Response:
[
  {
    "id": 19,
    "dataHora": "2026-04-23T01:00:03.418744857",
    "estat": "pendent",
    "tipus": "local",
    "idUsuari": 83,
    "idBotiga": 24,
    "linies": [
        {
            "idComanda": 19,
            "idProducte": 196,
            "quantitat": 1,
            "preuUnitari": 2.5
        }
    ]
  } 
]
```


### GET /api/comandes/botiga/[idBotiga]
- El client no te acces a aquesta funcionalitat
- El treballador te access a les de la seva botiga
- L'admin pot obtenir totes les comandes

```json
Header:
  Authorization: Bearer [token]

Response:
[
  {
    "id": 19,
    "dataHora": "2026-04-23T01:00:03.418744857",
    "estat": "pendent",
    "tipus": "local",
    "idUsuari": 83,
    "idBotiga": 24,
    "linies": [
        {
            "idComanda": 19,
            "idProducte": 196,
            "quantitat": 1,
            "preuUnitari": 2.5
        }
    ]
  } 
]
```


### PUT /api/comandes/[id]/estat/[estat]
Descripcio: actualitza l'estat de la comanda. L'estat es "pendent" inicialment quan l'usuari posa la comanda. El treballador i l'administrador poden canviar-lo a "processant" i finalment "completat".
> [!NOTE]
>  Trigger de comanda completada
Quan una comanda es marca com a completada el programa evalúa els punts i les insignies guanyades i actualitza aquesta informació.

```json
Header:
  Authorization: Bearer [token]

Response:
{
    "id": 21,
    "dataHora": "2026-04-23T01:33:41.180373",
    "estat": "completat",
    "tipus": "local",
    "idUsuari": 93,
    "idBotiga": 27,
    "linies": [
        {
            "idComanda": 21,
            "idProducte": 222,
            "quantitat": 1,
            "preuUnitari": 2.5
        }
    ]
}
```


### DELETE /api/comandes/[id]
Descripció: esborra una comanda.
- El client solament pot esborrar una comanda que encara no ha canviat d'estat (encara está pendent).
- L'administrador pot esborrar comandes en qualsevol estat.
- El treballador no pot esborrar cap comanda.

```json
Header:
  Authorization: Bearer [token]
```


### POST /api/comandes/[idComanda]/linies
Descripció: afegeix una linea a una comanda.
- El client pot afegir una linea a la seva propia comanda.

```json
Header:
  Authorization: Bearer [token]

Request:
{
    "idProducte": 222,
    "quantitat": 2,
    "preuUnitari": 2.5
}

Response:
{
    "idComanda": 21,
    "idProducte": 222,
    "quantitat": 2,
    "preuUnitari": 2.5
}
```


### DELETE /api/comandes/linies/[idComanda]/[idProducte]
Descripció: esborra una linea a una comanda.
- El client pot esborrar una linea a la seva propia comanda.
- Quan una comanda deixa de tenir lineas la comanda s'esborra.

```json
Header:
  Authorization: Bearer [token]
```

## Productes
### GET /api/productes
Descripció: retorna un llistat de tots els productes.
- El llistat es public, tothom pot accedir-hi.


``` json
Response:
[
    {
        "id": 209,
        "nom": "Espresso",
        "preu": 2.5,
        "categoria": "Café",
        "idBotiga": 24
    },
    {
        "id": 210,
        "nom": "Cappuccino",
        "preu": 3.5,
        "categoria": "Café",
        "idBotiga": 25
    },
    {
        "id": 211,
        "nom": "Croissant",
        "preu": 2.0,
        "categoria": "Pastry",
        "idBotiga": 26
    }
]
```

### GET /api/productes/botiga/[idBotiga]
Descripció: retorna un llistat de tots els productes d'una botiga amb id.
- El llistat es public, tothom pot accedir-hi.

``` json
Response:
[
    {
        "id": 209,
        "nom": "Espresso",
        "preu": 2.5,
        "categoria": "Café",
        "idBotiga": 26
    },
    {
        "id": 210,
        "nom": "Cappuccino",
        "preu": 3.5,
        "categoria": "Café",
        "idBotiga": 26
    },
    {
        "id": 211,
        "nom": "Croissant",
        "preu": 2.0,
        "categoria": "Pastry",
        "idBotiga": 26
    }
]
```

### GET /api/productes/[idProducte]
Descripció: retorna l'informació d'un producte amb el seu id.
- Aquesta informació es publica.

``` json
Response:
{
    "id": 209,
    "nom": "Espresso",
    "preu": 2.5,
    "categoria": "Café",
    "idBotiga": 26
}
```


### POST /api/productes
Descripció: crea un producte.
- Solament l'administrador pot crear un producte.

```json
Header:
  Authorization: Bearer [token]

Request:
{
    "nom": "Flat White Test",
    "preu": 3.75,
    "categoria": "Café",
    "idBotiga": 27
}

Response:
{
    "id": 221,
    "nom": "Flat White Test",
    "preu": 3.75,
    "categoria": "Café",
    "idBotiga": 27
}
```

### PUT /api/productes/[idProducte]
```json
Header:
  Authorization: Bearer [token]

Request:
{
    "nom": "Flat White Updated",
    "preu": 4.25,
    "categoria": "Café",
    "idBotiga": 27
}

Response:
{
    "id": 221,
    "nom": "Flat White Updated",
    "preu": 4.25,
    "categoria": "Café",
    "idBotiga": 27
}
```

### DELETE /api/productes/[idProducte]
Descripció: esborra un producte amb el seu id.
- Solament l'administrador pot esborrar un producte.

```json
Header:
  Authorization: Bearer [token]
```


## Botiga

### GET /api/botigues
Descripció: obtenir totes les botigues. 
- Tothom pot accedir-hi es informació publica.

```json
Response:
[
    {
        "id": 26,
        "nom": "Brewing Coffee",
        "adreca": "Carrer de l'Exemple, 1, Barcelona",
        "horari": "08:00-20:00"
    }
]
```

### GET /api/botigues/[id]
Descripció: obtenir una botiga per el seu id. 
- Tothom pot accedir-hi es informació publica.

```json
Response:
{
    "id": 26,
    "nom": "Brewing Coffee",
    "adreca": "Carrer de l'Exemple, 1, Barcelona",
    "horari": "08:00-20:00"
}
```

### POST /api/botigues
```json
Header:
  Authorization: Bearer [token]

Request:
{
    "nom": "Nova Botiga Test",
    "adreca": "Carrer Test 99",
    "horari": "09:00-18:00"
}

Response:
{
    "id": 27,
    "nom": "Nova Botiga Test",
    "adreca": "Carrer Test 99",
    "horari": "09:00-18:00"
}
```

### PUT /api/botigues/[id]
```json
Header:
  Authorization: Bearer [token]

Request:
{
    "nom": "Nova Botiga Test Updated",
    "adreca": "Carrer Test 99",
    "horari": "09:00-18:00"
}

Response:
{
    "id": 27,
    "nom": "Nova Botiga Test Updated",
    "adreca": "Carrer Test 99",
    "horari": "08:00-18:00"
}
```

## Badges i gamificació

### POST /api/insignies
Descripcio: Crea una nova insignia.
- Solament l'administrador pot crear insignies.

```json
Header:
  Authorization: Bearer [token]

Request:
{
    "nom": "Test Badge",
    "descripcio": "Insignia de proves",
    "imatgeUrl": "https://example.com/badge-test.png"
}

Response:
{
    "id": 85,
    "nom": "Test Badge",
    "descripcio": "Insignia de proves",
    "imatgeUrl": "https://example.com/badge-test.png"
}
```

### PUT /api/insignies/[idInsignia]
Descripcio: actualitza una insignia amb l'id.
- Solament l'administrador pot actualitzar insignies.

```json
Header:
  Authorization: Bearer [token]

Request:
{
    "nom": "Test Badge Updated",
    "descripcio": "Descripcio actualitzada",
    "imatgeUrl": "https://example.com/badge-updated.png"
}

Response:
{
    "id": 85,
    "nom": "Test Badge Updated",
    "descripcio": "Descripcio actualitzada",
    "imatgeUrl": "https://example.com/badge-updated.png"
}
```

### DELETE /api/insignies/[idInsignia]
Descripcio: esborra una insignia amb el seu id.
- Solament l'administrador pot esborrar insignies.

```json
Header:
  Authorization: Bearer [token]
```

### GET /api/gamificacion/levels
Descripció: obté informació dels nivells i els punts per passar al seguent.
- Aquesta informació publica.

```json
[
    {
        "nivell": 1,
        "punts": 0
    },
    {
        "nivell": 2,
        "punts": 100
    },
    {
        "nivell": 3,
        "punts": 250
    },
    {
        "nivell": 4,
        "punts": 500
    },
    {
        "nivell": 5,
        "punts": 1000
    }
]
```

### GET /api/insignies
Descripcio: obté totes les insignies disponibles. Aquesta informació es publica.

```json
Response:
[
    {
        "id": 81,
        "nom": "Matiner",
        "descripcio": "Demana cafè entre les 6h i les 8h",
        "imatgeUrl": null
    },
    {
        "id": 82,
        "nom": "Cafè espress",
        "descripcio": "Compra cafes espresso",
        "imatgeUrl": null
    },
    {
        "id": 83,
        "nom": "Gran Despesa",
        "descripcio": "Gasta 50€ o més en una sola comanda",
        "imatgeUrl": null
    },
    {
        "id": 84,
        "nom": "Recompenses de Fidelitat",
        "descripcio": "Completa 10 comandes",
        "imatgeUrl": null
    }
]
```

### GET /api/insignies/[idInsignia]
Descripció: Obté la informacio d'una insignia pel seu id.
- Aquesta informació es publica.

```json
Response
{
    "id": 81,
    "nom": "Matiner",
    "descripcio": "Demana cafè entre les 6h i les 8h",
    "imatgeUrl": null
}
```


### GET /api/badges/[badgeId]/triggers
Descripció: Obté tots els triggers relatius a una insignia.
- Solament l'administrador té acces als triggers.

```json
Header:
  Authorization: Bearer [token]

Response:
[
  {
    "id": 84,
    "idInsignia": 86,
    "triggerType": "TIME_OF_DAY",
    "hourStart": 6,
    "hourEnd": 8,
    "productCategory": null,
    "productId": null,
    "minSpendingAmount": null,
    "minOrderCount": null,
    "isActive": true
  }
]
```

### POST /api/badges/[badgeId]/triggers
Descripció: afegeix un trigger a una insignia, a partir del moment s'evaluará per aquesta condició cada vegada qu'una comanda es marqui com a completada.
- Solament l'administrador pot afegir triggers.

```json
Header:
  Authorization: Bearer [token]

Request:
{
    "triggerType": "TIME_OF_DAY",
    "hourStart": 6,
    "hourEnd": 8
}

Response:
[
  {
    "id": 84,
    "idInsignia": 86,
    "triggerType": "TIME_OF_DAY",
    "hourStart": 6,
    "hourEnd": 8,
    "productCategory": null,
    "productId": null,
    "minSpendingAmount": null,
    "minOrderCount": null,
    "isActive": true
}
]
```

### PUT /api/badges/[badgeId]/triggers/[triggerId]
Descripció: actualitza un trigger d'una insignia (pot desactivar-se enviant false per "isActive")
- Solament l'administrador pot afegir triggers.

```json
Header:
  Authorization: Bearer [token]

Request:
{
    "isActive": false
}

Response:
[
  {
    "id": 84,
    "idInsignia": 86,
    "triggerType": "TIME_OF_DAY",
    "hourStart": 6,
    "hourEnd": 8,
    "productCategory": null,
    "productId": null,
    "minSpendingAmount": null,
    "minOrderCount": null,
    "isActive": false
}
]
```

### DELETE /api/badges/[badgeId]/triggers/[triggerId]
Descripcio: esborra un trigger
- Solament l'administrador pot esborrar triggers.

```json
Header:
  Authorization: Bearer [token]
```


## Dades de demonstració

### POST /api/system/seed
Descripció: afegeix data demo per probar l'app. Solament l'administrador te acces a aquesta funcionalitat.

```json
Header:
  Authorization: Bearer [token]

Response:
  Demo data generated successfully.
```

### DELETE /api/system/resetDemo
Descripció: esborra totes les dades de demonstració.

```json
Header:
  Authorization: Bearer [token]

Response:
  Demo data removed.
```


## Esborrar tot
### DELETE /api/system/resetAll
> [!CAUTION]
> Aquesta funcionalitat esborra totes les dades incloent-hi les dades de producció: usuaris, productes, botigues, etc.

```json
Header:
  Authorization: Bearer [token]

Response:
  All data deleted.
```


> [!Note]
Com que utilitzem la mateixa instància, és possible que algú faci `resetAll` sense tornar a emplenar la base de dades. Fes una petició `POST /api/system/seed` abans de començar si vols tenir dades per visualitzar.




# DATABASE DIAGRAM
![Databse Diagram](https://github.com/slarhmich/coffriend-servidor/blob/8197b7b8da4b24609d06d58686f2f2f65e4690f0/coffriend_dia_db.png)
