# Sample data for majors and classes at the University of Oklahoma
majors_classes = {
    'Computer Science': ['CS 1013', 'CS 2033', 'CS 3053'],
    'Electrical Engineering': ['EE 1013', 'EE 2033', 'EE/CS 3023'],
    'Biology': ['BIO 1013', 'BIO 2023', 'BIO 3013']
}

# Function to retrieve major-specific classes
def get_major_classes(major):
    if major not in majors_classes:
        return "Please select a valid major."
    return majors_classes.get(major)

# Test case 1: Valid Major Selection Test
def test_valid_major():
    major = 'Computer Science'
    result = get_major_classes(major)
    expected = ['CS 1013', 'CS 2033', 'CS 3053']
    assert result == expected, f"Failed: Expected {expected}, but got {result}"
    print(f"Test 1 Passed: {major}")

# Test case 2: Invalid Major Selection Test (Unrelated Classes)
def test_invalid_classes():
    major = 'Computer Science'
    result = get_major_classes(major)
    unrelated_class = 'BIO 1013'
    assert unrelated_class not in result, f"Failed: {unrelated_class} should not be in {result}"
    print(f"Test 2 Passed: No unrelated classes in {major}")

# Test case 3: No Major Selected Test
def test_no_major_selected():
    major = None
    result = get_major_classes(major)
    expected = "Please select a valid major."
    assert result == expected, f"Failed: Expected {expected}, but got {result}"
    print("Test 3 Passed: No major selected")

# Test case 4: Major with Cross-listed Classes Test
def test_cross_listed_classes():
    major = 'Electrical Engineering'
    result = get_major_classes(major)
    expected = ['EE 1013', 'EE 2033', 'EE/CS 3023']
    assert result == expected, f"Failed: Expected {expected}, but got {result}"
    print(f"Test 4 Passed: Cross-listed classes for {major}")

# Run all tests
if __name__ == "__main__":
    test_valid_major()
    test_invalid_classes()
    test_no_major_selected()
    test_cross_listed_classes()
