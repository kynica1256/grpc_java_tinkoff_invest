Получение proto-файлов

```
git clone https://github.com/Tinkoff/invest-python

cd invest-python/protos/tinkoff/invest/

mv /grpc <нужная директория>
```

Установка proto-плагина

```
apt install protobuf-compiler protobuf-compiler-grpc

git clone https://github.com/grpc/grpc.git

cd grpc

git submodule update --init

make grpc_java_plugin

export PATH=$PATH:$(pwd)/bins/opt
```
Преобразование proto-файлов. Применяем к каждому proto-файлу.

```
protoc -I="./" --plugin=protoc-gen-grpc-java=/usr/bin/grpc_java_plugin --grpc-java_out="/root/<путь>/src/main/java/" --java_out="/root/<путь>/src/main/java/" "./<название>.proto"
```

Запуск
```
mvn exec:java -Dexec.mainClass=ru.tinkoff.App // Запуск скрипта
mvn compile // Коомпиляция
```





Директория Dev_methods рассчитана на ваши файлы, методы и т.д.

Файлы Dev_data.java и Dev_logic.java являются необходимыми для корректной работы скрипта.
В файле Dev_logic.java необходимо заполнить переменные следующим образом:


```
Dev_logic.java

String mini_figi = "<FIGI>"; // figi, который будет активен для просматривания
String token = "<TOKEN>"; // Ваш токен тинькофф инвестиции
String[] figi = new String[]{"<FIGI>", "<FIGI>", "<FIGI>", ...}; // Цены на figi, которые будут переданы 
```




В файл Dev_data.java информация о стакане и текущая цена передаётся в метод - run_method.

Стакан передаётся в run_method следующим образом:


```
[
  [...], // Цены по bids float
  [...], // Количетсво лотов по bids float
  [...], // Цены по ask float
  [...] // Количетсво лотов по ask float
[
```




Доступные методы:

```
String account_id = "<id>";
String ticker_money = "<ticker>"; // EUR or RUB or USD
String figi = "<figi>";
new Users_Methods().buy_method(account_id, ticker_money, figi); // Ордер на покупку
new Users_Methods().sale_method(account_id, ticker_money, figi); // Ордер по продажу
new Users_Methods().get_money(account_id, ticker_money); //Получение баланса
```
