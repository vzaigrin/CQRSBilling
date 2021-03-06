# CQRS Billing
Проектная работа курса [Scala разработчик](https://otus.ru/lessons/scala/) от Otus.

Использование шаблона CQRS для системы биллинга условной телекоммуникационной компании

## Постановка задачи
Из Kafka поступают данные о звонках, текстовых сообщениях и потреблённом объёме траффика Интернет:
- *call* - topic с информацией о звонках
- *text* - topic с информацией о смс
- *web* - - topic с данными о трафике Интернет

Данные надо проверить, сохранить и предоставить доступ к ним.

## Реализация
Kafka, Cassandra и PostgreSQL запускаются в Docker.

- **Writer** принимает потоки из Kafka, фильтрует их и в виде потока сообщений помещает в Cassandra:
	- Alpakka Kafka
	- Akka Streams
	- Akka Persistence
	- Circe
- **Reader** читает поток сообщений из Cassandra и обновляет данные в СУБД PostgreSQL.
- **Query** предоставляет REST интерфейс для доступа к данным:
	- Akka HTTP
	- tapir
	- Slick
- **Generator** создаёт поток данных в Kafka для тестирования:
	- ZIO Config
	- ZIO Kafka
	- ZIO Logging
	- Circe
