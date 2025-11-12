import csv
import os

def get_csv_cell(file_path, row_index, col_index):
    try:
        with open('/Users/drago/OneDrive/Desktop/College 2025-2026/TrojanDefense.csv', mode='r', newline='', encoding='utf-8') as csvfile:
            reader = csv.reader(csvfile)
            for i, row in enumerate(reader):
                if i == row_index:
                    if col_index < 0 or col_index >= len(row):
                        raise IndexError(f"Column index {col_index} out of range for row {i}.")
                    return row[col_index]
        # If loop completes without returning, row not found
        raise IndexError(f"Row index {row_index} out of range.")
    except csv.Error as e:
        raise ValueError(f"Error reading CSV file: {e}")
    
with open('/Users/drago/OneDrive/Desktop/College 2025-2026/TrojanDefense.csv', mode='r') as file:
    csv_reader = csv.reader(file)
    for row in csv_reader:
        print(row)
    value = get_csv_cell(csv_reader, row_index=10, col_index=5)

try:
    with open("/Users/drago/OneDrive/Desktop/College 2025-2026/output.txt", "w", encoding="utf-8") as f:
        print(f"INSERT({value})", file=f)
    print("Data successfully written to output.txt")
except OSError as e:
    print(f"File error: {e}")