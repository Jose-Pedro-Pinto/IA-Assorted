Write in shell:
	javac trab4.java
	java trab4
Windows 8.1(64 bit)
java 1.8.0_151
Examples of training data:
restaurant.csv
weather.csv
iris.csv
Examples of test data:
for restaurant:
	example2.csv  has the correct class value-will calculate accuracy
	example3.csv  has ? in class value-will ignore
	example4.csv  no class value-will ignore; same output as before
for iris:
	example1.csv  to show what happens with numeric values

both training data and test data must have a first line with the name of the atributes and a firs column with the name of the test
test data may or not have the goal\class column
training data must have the goal\class column
'?' anywhere other than the goal\class data will be treated as any other atribute
incorrect file names will crash the program