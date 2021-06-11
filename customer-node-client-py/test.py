import virtualbox, time
from bash import bash
from virtualbox.library import NetworkAttachmentType

vbox = virtualbox.VirtualBox()
machine_name = "Ubuntu_k8s_node_1"

print("Machines:")
for machine in vbox.machines:
    print(machine.name, machine.state, machine.session_state, machine.default_frontend, machine.cpu_count, machine.cpu_execution_cap, machine.memory_size, machine.memory_balloon_size)
print()

# session = virtualbox.Session()
# for machine in vbox.machines:
#     if machine.name == "Ubuntu_k8s_node_2" or machine.name == "Ubuntu_k8s_node_1":
#         if str(machine.state) != "FirstOnline":
#             print("Start %s" % machine.name)
#             progress = machine.launch_vm_process(session, "headless", [])
#             progress.wait_for_completion()

# session = virtualbox.Session()
# machine = vbox.find_machine("Ubuntu_k8s_node_2")
# # progress = machine.launch_vm_process(session, "gui", "")
# # For virtualbox API 6_1 and above (VirtualBox 6.1.2+), use the following:
# # progress = machine.launch_vm_process(session, "headless", [])
# # progress.wait_for_completion()
#
# print(machine.settings_file_path)

# machine = vbox.create_machine(name="test")
# machine.cpu_count = 2
# machine.cpu_execution_cap = 80
# machine.memory_size = 1024
# with machine.create_session() as session:
#     adapter = session.machine.get_network_adapter(0)
#     adapter.attachment_type = NetworkAttachmentType.Bridged
#     session.machine.save_settings()
# vbox.register_machine(machine)


# Create new IAppliance and read the exported machine
# called 'ubuntu'.
appliance = vbox.create_appliance()
appliance.read("/home/boris/Documents/Ubuntu_k8s_node_1.ova")

# Extract the IVirtualSystemDescription object
# for 'ubuntu' and set its name to 'foobar' and cpu '2'.
desc = appliance.find_description('Ubuntu_k8s_node_1')
desc.set_name('test')
desc.set_cpu('2')

# perform import
p = appliance.import_machines()
p.wait_for_completion()


# machine = vbox.create_machine('node2.xml', 'node2-test', [], 'Ubuntu_64', '')
# vbox.register_machine(machine)

# session = virtualbox.Session()
# machine = vbox.find_machine('node2-test')
# print(machine.session_state, machine.session_pid)
# # bash('kill %s' % machine.session_pid)
# # session = machine.create_session()
# # print(session.machine.name)
# # session.unlock_machine()
# progress = machine.launch_vm_process(session, "headless", [])
# progress.wait_for_completion()
# time.sleep(10)
# session.console.power_down()
