from kubedom.vboxapi.api import create_machine, start, stop, list_machines
import time

ova_file = "/home/boris/Documents/Ubuntu_k8s_node_1.ova"
name = 'test'

create_machine(ova_file, name)
start(name)
for machine in list_machines():
    print(machine.name, machine.state, machine.session_state, machine.cpu_count, machine.cpu_execution_cap, machine.memory_size)
time.sleep(180)
stop(name)
