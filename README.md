# Rota de Viagem #

Um turista deseja viajar pelo mundo pagando o menor preço possível independentemente do número de conexões necessárias.
Esse aplicativo visa facilitar ao nosso turista, escolher a melhor rota para sua viagem.
As rotas serão carregadas pela aplicação a partir de um arquivo de entrada.


## Exemplo de arquivo de entrada CSV ##
```csv
GRU,BRC,10
BRC,SCL,5
GRU,CDG,75
GRU,SCL,20
GRU,ORL,56
ORL,CDG,5
SCL,ORL,20
```


## Arquitetura ##
Projeto foi construído em Java 11 - Spring Boot.
Foram adotas convenções de desenvolvimento comuns do mercado para a estruturação da aplicação.
A partir de root/src/main/java/com/project/springboot/bestRoute temos:
- config -> Classe de configuração da documentação Swagger
- controller -> Classe de entrada para os endpoints da Api Rest
- mapper -> Responsável por realizar as conversões de dados entre Models e outros formatos e vice-versa, e a validação dos mesmos
- model -> Modelos utilizados referenciando os dados utilizados na entrada, processamento e saída de dados da aplicação
- repository -> Responsável por orquestrar o carregamento, persistência e manipulação de dados
- service -> Core lógico responsável pelas regras de negócio: inicialização, processamento de consulta ou adição de rota, funcionamento da interface do console.

Para a parte lógica que processa a melhor rota foi utilizada uma adaptação do [Algoritmo de Dijkstra](https://pt.wikipedia.org/wiki/Algoritmo_de_Dijkstra), aonde os pontos de origem e destino são convertidos em vértices em um grafo com suas respectivas "distâncias" (nesse caso custo), e a partir do processamento do mesmo se dá o resultado esperado de "menor distância". 



## Testes Unitários ## 
Aplicação conta com cobertura bastante completa de teste unitários do Spring realizados com JUnit e Mockito.
Na construção do projeto os mesmos serão executados garantido a estabilidade da aplicação.



## Construção do projeto ##
Será necessário biblioteca JVM versão 11 ou superior rodar corretamente o aplicativo.
Fazer a construção do projeto atravez do Maven Wrapper contido:
```shell
$ ./mvnw clean install
```


## Execução do programa ##
A inicializacao do teste se dará por linha de comando onde o único argumento é o arquivo com a lista de rotas inicial.
```shell
$ java -jar target/bestRoute-1.0.jar input-routes.csv
```
OBS: Por padrão a execução está configurada no resource para não gerar log no Console, apenas no arquivo de log criado em (root/logger/app.log). Caso deseja visualizar toda a informação registrada do SpringBoot e Tomcat no console, remover comentário citado.

Quando projeto estiver de pé pode-se utilizar o console ou a interface Rest para realizar os comandos de entrada.

- Interface do Console
Para utilizar o console como input, fazer a entrada de informações no formato "DE-PARA" utilizando as siglas de localizações, com o padão ABC-DEF.
Exemplo:
  ```shell
  please enter the route: GRU-CGD
  best route: GRU - BRC - SCL - ORL - CDG > $40
  please enter the route: BRC-CDG
  best route: BRC - ORL > $30
  ``` 
  
- Interface Rest
Pela interface Rest é possível realizar a mesma consulta anterior com um resultado mais completo e legível no formato Json, e
também fazer adição de novas Rotas com seu respectivo valor.
Verificação dos padrões de entrada e saída da API, assim como também o teste da mesma ficam disponíveis na documentação [Swagger](http://localhost:8080/swagger-ui.html)

