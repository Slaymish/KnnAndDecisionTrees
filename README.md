# COMP Assignment 1

Hamish Burke

## To run part 1

- Training file: `data/part1/wine_train.csv`
- Testing file: `data/part1/wine_test.csv`
- Output file: `data/part1/output.csv`
- k: 3

_In the root directory of the project_

```bash
java -jar out/artifacts/COMPAssignment1_jar/Part1_Knn.jar data/part1/wine_train.csv data/part1/wine_test.csv data/part1/output.csv 3 
```

The accuracy will be printed to the console.

## To run part 2

- Training file: `data/part2/rtg_X.csv`
- Output file: `data/part2/DT_X.txt`

_In the root directory of the project_

For RTG_A:
```bash
java -jar out/artifacts/COMPAssignment1_jar2/Part2_DT.jar data/part2/rtg_A.csv data/part2/DT_A.txt
```

For RTG_B:
```bash
java -jar out/artifacts/COMPAssignment1_jar2/Part2_DT.jar data/part2/rtg_B.csv data/part2/DT_B.txt
```