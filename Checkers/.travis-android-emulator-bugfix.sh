#!/bin/bash
- echo no | android create avd --force -n test -t android-25 --abi armeabi-v7a
- emulator -avd test -no-skin -no-audio -no-window &
- android-wait-for-emulator
- adb shell input keyevent 82 &