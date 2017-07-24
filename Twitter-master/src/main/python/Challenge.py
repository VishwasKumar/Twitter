import json
import sys
import os.path as path
import itertools


# Gets all the combinations of owners required to sign off for the commit
def getCombinations(owner_list):
    # get all combinations possible given a list of lists of owners (So this will give all the combinations possible)
    itr = list(itertools.product(*owner_list))
    # we will store our final result of owners in this
    result = []
    # If there are any redundancies we store it here and flatten it out later by removing redunduncy
    flat = []
    # This will store the combinations that do not have redundancy
    rest = []

    # check if in any of the combinations there are repeated owners store in flat
    for i in itr:
        rest.append(i)
        if len(i) != len(set(i)):
            flat.append(set(i))

    # if flat size is 0 then there is no redundancy
    if len(flat) == 0:
        return rest
    super_owner = [x for x in flat if len(x) == 1]

    if len(super_owner) != 0:
        flat = super_owner
        result.extend(tuple(super_owner))
    else:
        result.extend(flat)

    # remove redundancy.
    success = False
    for r in rest:
        for f in flat:
            if not all(x in r for x in list(f)):
                success = True
            else:
                success = False
                break
        if success:
            result.append(r)
    return result


# get the owners from the json file. if files do not have owners go one level up and get the parents owners
def getOwners(dir, data):
    if len(data[str(dir)]["owners"]) != 0:
        return data[str(dir)]["owners"]
    else:
        if not data[str(dir)]["parent"]:
            return []
        return getOwners(data[str(dir)]["parent"], data)


def get_possible_approvers(dir, data):
    owner_list = []
    own = getOwners(dir, data)

    if len(data[str(dir)]["dependencies"]) == 0:
        return [own[i:i+1] for i in range(0, len(own), 1)]
    
    owner_list.append(own)
    for dependent in data[str(dir)]["dependencies"]:
        owner_list.append(getOwners(str(dependent), data))
    return getCombinations(owner_list)


# main function where I am parsing inputs from the command line
# the input format : Challenge.py -approvers approver1 approver2 -changed-files path/to/file
def main():
    # These are the list splice indices to read the list of approvers and changed files
    approverStartIdx = None
    approverEndIdx = None

    if '-approvers' in sys.argv:
        approverStartIdx = sys.argv.index('-approvers') + 1
        if '-changed-files' in sys.argv:
            approverEndIdx = sys.argv.index('-changed-files')

    approvers = sys.argv[approverStartIdx: approverEndIdx]

    approvers = [x.lower() for x in approvers]
    # Instead of having a new splice index for files, work with what we already have.
    # An improvise of approverEndIdx can serve the right purpose for extracting files as well.
    files = sys.argv[(approverEndIdx + 1):]

    # parsing json
    with open('dependencyGraph.json') as data_file:
        data = json.load(data_file)

    success = False
    # For each file check if the right approvers are present, if so record it
    for file in files:
        dir = path.dirname(file)
        possible_approvers = get_possible_approvers(dir, data)
        success = False
        for possible in possible_approvers:
            if set(possible).issubset(approvers):
                success = True
                break
        if not success:
            break
    if success:
        print("Approved")
    else:
        print("Insufficient approvals")


if __name__ == "__main__":
    main()
