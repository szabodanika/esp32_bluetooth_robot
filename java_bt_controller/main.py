with open('/dev/cu.ESP32', 'w+', 0) as bt:
    while (True):
        bt.write("Hi\n")
        print(bt.readline())
