import sys
import os
import json
import io
import colorama
from colorama import Fore, Style

defaultLimbMapping = {
    'LeftLeg': 'left_leg',
    'RightLeg': 'right_leg',
    'LeftArm': 'left_arm',
    'RightArm': 'right_arm',
    'Torso': 'torso',
    'Head': 'head',

    'Head2': 'head2',
    'LeftArm2': 'left_arm2',
    'RightArm2': 'right_arm2',
    'LeftArm3': 'left_arm3',
    'RightArm3': 'right_arm3',
    'Abdomen': 'abdomen',
    'LowerTorso': 'lower_torso'
}

def convertToLimbReference(name):
    if name in defaultLimbMapping.keys():
        return defaultLimbMapping[name]
    value = input(f"Unrecognized limb name {name}, enter serialized name (i.e. left_arm) or leave empty to skip: ")
    defaultLimbMapping[name] = value
    return value

def convertToJavaLimb(serialName):
    return f"Limb.{serialName.upper()}"

def convertToChanged(bbAnimJson):
    changedJson = { 'length': bbAnimJson['length'], 'transitionDuration':
        float(not auto and input('Enter transition duration (seconds in/out to smooth player action) [1.25]: ') or 1.25) }
    changedChannels = {}

    for bbAnimator in bbAnimJson['animators'].values():
        if bbAnimator['type'] != 'bone':
            print(f"Skipping {bbAnimator['name']}")
        limb = convertToLimbReference(bbAnimator['name'])
        if not limb:
            print(f"Skipping {bbAnimator['name']}")
            continue

        changedTargets = { 'position': [], 'rotation': [] }
        for bbKeyframe in bbAnimator['keyframes']:
            target = bbKeyframe['channel']
            if target != 'position' and target != 'rotation':
                print(f"{Fore.YELLOW}WARN: Unusable target {target}")
                continue

            dataPointName = 'position'
            if target == 'rotation':
                dataPointName = 'degrees'

            bbDatapoint = bbKeyframe['data_points'][0]
            changedTargets[target].append({ 'time': bbKeyframe['time'],
                                            dataPointName: [ float(bbDatapoint['x']), float(bbDatapoint['y']), float(bbDatapoint['z']) ],
                                            'interpolation': bbKeyframe['interpolation'] })

        changedChannel = []
        for target, keyframes in changedTargets.items():
            if len(keyframes) == 0:
                continue
            changedChannel.append({ 'target': target, 'keyframes': sorted(keyframes, key=lambda x: x['time']) })

        changedChannels[limb] = changedChannel

    changedJson['channels'] = changedChannels
    return changedJson

def jsonToJava(name, changedJson):
    animations = ""

    for limb, targets in changedJson['channels'].items():
        for target in targets:
            animations += f"\n\t\t.addAnimation({convertToJavaLimb(limb)}, new AnimationChannel(AnimationChannel.Target.{target['target'].upper()},"
            for keyframe in target['keyframes']:
                dataPointName = 'position'
                funcRef = 'KeyframeAnimations.posVec'
                if not 'position' in keyframe:
                    dataPointName = 'degrees'
                    funcRef = 'KeyframeAnimations.degreeVec'
                animations += (f"\n\t\t\tnew Keyframe({keyframe['time']}F, {funcRef}("
                               f"{keyframe[dataPointName][0]}F, {keyframe[dataPointName][1]}F, {keyframe[dataPointName][2]}F"
                               f"), AnimationChannel.Interpolation.{keyframe['interpolation'].upper()}),")
            animations = animations[:-1]
            animations += "))"

    return (f"public class Definition {{\n\tpublic static final AnimationDefinition {name} = "
            f"AnimationDefinition.Builder.withLength({changedJson['length']}F).withTransition({changedJson['transitionDuration']}F){animations}.build();\n}}")

def printUsage():
    print("CLI tool to extract animations in a BlockBench project into the required format for Java or JSON.")
    print("Resulting definitions are compatible with Changed: Minecraft Mod.")
    print("Usage:\n\textract bbProject [auto java/json] [dir outDir]\n")
    print("\tbbProject - BlockBench project with animations saved within")
    print("\tauto      - Indicates the script should pick which animation and save with no user input")
    print("\tdir       - Specifies the output directory to save animations\n")
    print("Examples:")
    print("\textract project.bbmodel                 : Will prompt to select which animations to export, what format, and resolve issues")
    print("\textract project.bbmodel auto json       : Will deduce automatically which animations to export into JSON, failing if issues present")
    print("\textract project.bbmodel dir animations/ : Will prompt to select which animations to export with 'animations/' being the root directory")

def inputFor(prompt, options, default=None):
    while True:
        userInput = input(prompt)
        if userInput in options:
            return userInput
        elif not userInput and default:
            return default
        else:
            print(f"Unexpected value: {userInput}")

if len(sys.argv) < 2:
    printUsage()
    sys.exit()

bbProjectPath = sys.argv[1]

exportPath = ""

auto = False
autoFmt = "json"

index = 2
while len(sys.argv) >= index + 1:
    if sys.argv[index] == "auto":
        index += 1
        if len(sys.argv) < index + 1:
            print(f"{Fore.RED}FATAL: Missing auto format{Style.RESET_ALL}")
            sys.exit()
        auto = True
        autoFmt = sys.argv[index]
        if autoFmt != "java" and autoFmt != "json":
            print(f"{Fore.RED}FATAL: Invalid format \"{autoFmt}\"{Style.RESET_ALL}")
            sys.exit()
    elif sys.argv[index] == "dir":
        index += 1
        if len(sys.argv) < index + 1:
            print(f"{Fore.RED}FATAL: Missing output path{Style.RESET_ALL}")
            sys.exit()
        exportPath = sys.argv[index]
    index += 1

with open(bbProjectPath, 'r') as file:
    bbProject = json.load(file)

print(f"Loaded BlockBench project {bbProject['name']}")

allAnimations = bbProject['animations']
if (len(allAnimations) == 0):
    print(f"{Fore.RED}FATAL: Project has no animations.{Style.RESET_ALL}")
    sys.exit()

for selectedAnimation in allAnimations:
    if not auto:
        choice = inputFor(f"Export \"{selectedAnimation['name']}\"? [Y/n]: ", [ "y", "Y", "n", "N" ], "y")
        if choice == "n" or choice == "N":
            continue

    resultJson = convertToChanged(selectedAnimation)
    print(f"Extraction to Changed format complete")
    outFile = ((not auto and input(f"Save \"{selectedAnimation['name']}\" as? [{exportPath}{selectedAnimation['name']}.{autoFmt}]: ")) or
               f"{exportPath}{selectedAnimation['name']}.{autoFmt}")

    if outFile.endswith('java'):
        with open(outFile, 'w') as file:
            file.write(jsonToJava(selectedAnimation['name'], resultJson))
            print(f"Saved as {outFile}")
    elif outFile.endswith('json'):
        with open(outFile, 'w') as file:
            json.dump(resultJson, file)
    else:
        print(f"{Fore.YELLOW}WARN: Export file format is not java or json, defaulting to JSON contents.{Style.RESET_ALL}")
        with open(outFile, 'w') as file:
            json.dump(resultJson, file)

print(f"{Fore.CYAN}Extraction Complete{Style.RESET_ALL}")
