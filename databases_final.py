import csv
import os

TREE_COUNT_COL = 6
TREE_STATUS_COL = 3
SYMBOL_COL = 5
DBH_COL = 7
HEIGHT_COL = 8
RADIAL_GROWTH_COL = 11
AGE_COL = 14
CROWN_COVERAGE_COL = 15
SNAG_DECAY_COL = 19
    
def make_death_insert_statement():
    try:
        with open('/Users/drago/OneDrive/Desktop/College 2025-2026/TrojanDefense.csv', mode='r') as file:
            csv_reader = csv.reader(file)
            with open("/Users/drago/OneDrive/Desktop/College 2025-2026/output.txt", "w", encoding="utf-8") as f:
                index = 0
                row_index = 0
                col_index = 0
                for row in csv_reader:
                    if(row[TREE_COUNT_COL].isdigit()):
                        if(int(row[TREE_COUNT_COL]) == 1):
                            index += 1
                            if(row[TREE_STATUS_COL] == 'D'):
                                print(f"INSERT INTO DEATH (TreeID, SnagDecay) VALUES ({index}, {row[SNAG_DECAY_COL]});", file=f)
            print("Data successfully written to output.txt")
    except OSError as e:
        print(f"File error: {e}")

def make_life_insert_statement():
    try:
        with open('/Users/drago/OneDrive/Desktop/College 2025-2026/TrojanDefense.csv', mode='r') as file:
            csv_reader = csv.reader(file)
            with open("/Users/drago/OneDrive/Desktop/College 2025-2026/output.txt", "w", encoding="utf-8") as f:
                index = 0
                row_index = 0
                col_index = 0
                for row in csv_reader:
                    if(row[TREE_COUNT_COL].isdigit()):
                        if(int(row[TREE_COUNT_COL]) == 1):
                            index += 1
                            if(row[TREE_STATUS_COL] == 'L'):
                                if(row[RADIAL_GROWTH_COL] == '' and row[AGE_COL] != ''):
                                    print(f"INSERT INTO LIFE (TreeID, RadialGrowth, Age, Height, CrownCoverage) VALUES ({index}, null, {row[AGE_COL]}, {row[HEIGHT_COL]}, {row[CROWN_COVERAGE_COL]});", file=f)
                                elif(row[AGE_COL] == '' and row[RADIAL_GROWTH_COL] != ''):
                                    print(f"INSERT INTO LIFE (TreeID, RadialGrowth, Age, Height, CrownCoverage) VALUES ({index}, {row[RADIAL_GROWTH_COL]}, null, {row[HEIGHT_COL]}, {row[CROWN_COVERAGE_COL]});", file=f)
                                elif(row[RADIAL_GROWTH_COL] != '' and row[AGE_COL] != ''):
                                    print(f"INSERT INTO LIFE (TreeID, RadialGrowth, Age, Height, CrownCoverage) VALUES ({index}, {row[RADIAL_GROWTH_COL]}, {row[AGE_COL]}, {row[HEIGHT_COL]}, {row[CROWN_COVERAGE_COL]});", file=f)
                                elif(row[AGE_COL] == '' and row[RADIAL_GROWTH_COL] == ''):
                                    print(f"INSERT INTO LIFE (TreeID, RadialGrowth, Age, Height, CrownCoverage) VALUES ({index}, null, null, {row[HEIGHT_COL]}, {row[CROWN_COVERAGE_COL]});", file=f)
            print("Data successfully written to output.txt")
    except OSError as e:
        print(f"File error: {e}")


def make_purgatory_insert_statement():
    try:
        with open('/Users/drago/OneDrive/Desktop/College 2025-2026/TrojanDefense.csv', mode='r') as file:
            csv_reader = csv.reader(file)
            with open("/Users/drago/OneDrive/Desktop/College 2025-2026/output.txt", "w", encoding="utf-8") as f:
                index = 0
                row_index = 0
                col_index = 0
                for row in csv_reader:
                    if(row[TREE_COUNT_COL].isdigit()):
                        if(int(row[TREE_COUNT_COL]) == 1):
                            index += 1
                            print(f"INSERT INTO PURGATORY (TreeID, Symbol, Status, DBH) VALUES ({index}, \'{row[SYMBOL_COL]}\', \'{row[TREE_STATUS_COL]}\', {row[DBH_COL]});", file=f)
            print("Data successfully written to output.txt")
    except OSError as e:
        print(f"File error: {e}")
make_death_insert_statement()
#make_life_insert_statement()
#make_purgatory_insert_statement()