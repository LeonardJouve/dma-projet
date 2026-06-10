
Notre projet consiste à créer une application Android utilisant la technologie des ultrasons.

L’objectif de l’application est de faire un système de timbrage pour une entreprise sans badge, directement depuis son téléphone. Pour cela, nous aurions une application principale sur le téléphone des employés qui servirait d’émetteur, et une application secondaire utilisée comme récepteur pour simuler la timbreuse.

La fonctionnalité principale serait de permettre à l’application des employés de communiquer avec la timbreuse afin de badger / dé-badger. La timbreuse déterminerait l’ID de l’employé et mémoriserait la date et l’heure de chaque timbrage, permettant ainsi de calculer les horaires de travail.

Nous pensions utiliser le SDK Radius de LISNR [1] pour la communication en ultrasons, mais cette solution est malheureusement sous licence. Nous avons alors choisi ggwave [2] comme alternative. C’est une librairie plus bas niveau de data over sound qui permet tout de même l’échantillonnage de données en ultrasons mais à un débit très faible (16 b/sec). Cela devrait tout de même être suffisant car l’utilisateur a très peu de données à transmettre au moment du timbrage. Pairsonic [3] est un exemple d’utilisation de cette librairie dans une application android pour échanger des informations entre devices.

Nous aimerions également pouvoir authentifier les messages envoyés avec une signature cryptographique afin de vérifier l’identité des employés. Cela pourrait potentiellement poser des soucis car ça risque d’augmenter fortement la quantité de données à transmettre, sachant que le débit de transmission reste très faible.

[1] https://lisnr.com/radius-ultrasonic-sdk-3/
[2] https://github.com/ggerganov/ggwave
[3] https://github.com/seemoo-lab/pairsonic
