README for tumor-diagnose
by Robert Colgan
erobertc92@gmail.com
rec2111@columbia.edu

This program is an implementation of the k-nearest neighbor algorithm applied
to the diagnosis of tumors as malignant or benign. It is written in Java and 
was completed for COMS W1004 "Introduction to Computer Science/Programming in 
Java" taught by Adam Cannon at Columbia University in the fall of 2010. 

The program uses data from the Wisconsin Breast Cancer Database available from
http://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Diagnostic%29
as training data and to test its accuracy. It reads in the Database's list of 
549 tumors and their characteristics and whether each tumor was malignant or
benign (included in the repository as wdbc.data) and randomly separates 80% of 
the tumors to be used as training data and the remaining 20% to be used to test
its accuracy. Once the training data has been read, for each tumor in the test
data, it sorts the tumors in the training data by Pythagorean distance from 
the test tumor (using each of the tumors' 30 characteristics). After sorting,
the program decides whether the test tumor is malignant or benign based on the
majority of the k closest tumors, and compares the result to the actual tumor
from the database. After this is completed for all tumors in the test data, it
calculates the overall accuracy for that iteration. The program runs 100 trials
of the entire process, dividing up the tumor data into training and test data
randomly every time, and compiles and reports the overall average accuracy.

There are two versions of the program: the first, which is run by calling 
NearestNeighborTest, runs the above calculations for k = 1 (i.e. it guesses the
type of tumor based only on the closest neighbor). The second version, written
for extra credit on the assignment, is run by calling KNearestNeighborTest. It 
runs the same test for values of k = 3, k = 5, and k = 7, and reports the 
accuracy for each value of k. Both versions prompt the user for the path to the
tumor data file when run; it is included in the repository as wdbc.data.

The accuracy ranges from around 91% for k = 1 up to around 93% for k = 7. 
KNearestNeighbor also reports the accuracy of the guess when the k nearest 
tumors were unanimous and when they didn't all have the same type, which was 
not for credit but which I thought added an interesting element to the results.
For k = 3 the program is generally around 96% accurate when the three nearest
neighbors are unanimous and around 64% accurate when they are not; for k = 7 
the results are around 97% accurate for all unanimous versus around 76% 
accurate when they are not. Increasing k brings up the accuracy for when the 
nearest neighbors aren't unanimous, raising the overall average, but doesn't
affect the accuracy nearly as dramatically when they are unanimous. 

Using the program requires some patience -- the runtime for k = 7 can be up to
two minutes. 

Some notes on the structure of the program:
NearestNeighbor class: a repository for static methods used in the nearest 
neighbor algorithm (two of which, Accuracy100Trials and Import, are called by 
the main methods of other classes and the other three of which (Accuracy, 
FindTypeOfNthNearestNeighbor, and SortByDistanceFrom) are called inside other 
methods in NearestNeighbor, which I did to make my code easier to write and
understand because of the greater level of abstraction this creates. 
SortByDistanceFrom takes a single Tumor object and array of Tumor objects and 
sorts the array by calculating each Tumor in the array's distance from the 
single Tumor object and making a new array of Tumors with distance information,
then sorting this new array (making use of the compareTo method in the Tumor 
class). It returns this sorted array. 
FindTypeOfNthNearestNeighbor takes an integer N, a single Tumor object, and 
an array of Tumor objects and finds the Nth nearest neighbor to the single 
Tumor object in the array of Tumor objects, and reports back the type of that 
neighbor. It does this by calling SortByDistanceFrom, giving it the single 
Tumor object and the array of Tumor objects, then picking the Nth value 
from the array returned. 
Accuracy takes a value for k and an array of Tumors and runs one trial of the 
k-nearest neighbor algorithm (for k-nearest neighbor) by dividing up the array
of Tumors into the first 80% (for training data) and the last 20% (for 
testing data). Then, for each tumor in the testing data, it tests the k-nearest
neighbor algorithm for the value of k provided by feeding each Tumor from the 
testing data and the array of Tumors for training data (as well as the value 
for k) into FindTypeOfNthNearestNeighbor, then checking the result returned 
against the actual type of that tumor. (For k > 1, it runs 
FindTypeOfNthNearestNeighbor for 1 through k, and makes its guess based on 
whether there were more malignants than benigns or vice-versa.) Once the method
is done doing this for all values in the testing data, it returns a percentage
accuracy. 
Accuracy100Trials simply runs Accuracy 100 times and keeps track of the 
results, reporting them. Originally I just had this written out in the main
method, but I made it a static method and moved it to NearestNeighbor to 
shorten my main methods and because otherwise I would have had to write it out 
three times for each value of k in KNearestNeighbor.

Tumor class: Models a tumor. Contains variables for ID number (which I decided
not to discard for debugging purposes -- for a while when the Dist method was
returning bad results for certain tumors, having the ID number was helpful), 
a boolean for the type (true for malignant, false for benign), and then either
an array of 30 doubles for the remaining characteristics, or a single double 
for the distance between this Tumor and another Tumor. It contains accessor 
methods for the ID number, type, and characteristics, as well as a Dist method,
which calculates the distance between this Tumor and another Tumor supplied as 
an argument. 
When Prof. Cannon said in class that instead of doing the two-dimensional array
for the characteristics of tumors we could also have objects for each 
observation, I decided that that would be a more intuitive way of structuring 
my program. Each Tumor has an array for all of its characteristics, and then
we work with arrays of Tumor objects, instead of a more abstract two-
dimensional array, which I thought would make it harder to think about what
each value represents. 

I then have two classes with main methods, one for the regular assignment and 
one for the extra credit. The NearestNeighborTest class just runs 
NearestNeighbor.Accuracy100Trials once for k = 1 and reports the results. The 
KNearestNeighborTest runs NearestNeighbor.Accuracy100Trials three times for 
three different values of k and reports the results for each. I decided to have
two main methods to keep my classes for each part of the assignment as simple 
as possible. 