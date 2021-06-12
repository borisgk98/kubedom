"""pyvbox: How to copy a file from a VM.
"""
import os
import virtualbox

# Assume machine is already running.
vbox = virtualbox.VirtualBox()
machine = vbox.find_machine("customer-node")
storage = machine.get_storage_controller_by_name("SATA")
print(storage)
# session = machine.create_session()
#
# # copy notepad.exe to ./notepad.exe
# gs = session.console.guest.create_session("vm-provider", "vm-provider")
# gs.copy_from("/home/boris/date.txt", "/home/vm-provider/test.txt")
# gs.close()