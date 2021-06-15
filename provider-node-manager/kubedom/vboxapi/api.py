import logging

import virtualbox
from bash import bash
from virtualbox.library import CleanupMode

vbox = virtualbox.VirtualBox()
__MACHINE_INNER_NAME__ = 'customer-node'


def create_machine(ova_location, machine_name, inner_name=__MACHINE_INNER_NAME__):
    logging.info("Create machine %s" % machine_name)

    # Create new IAppliance and read the exported machine
    # called 'ubuntu'.
    appliance = vbox.create_appliance()
    appliance.read(ova_location)

    # TODO set custom properties
    # Extract the IVirtualSystemDescription object
    # for 'ubuntu' and set its name to 'foobar' and cpu '2'.
    desc = appliance.find_description('customer-node')
    desc.set_name(machine_name)
    # desc.set_cpu('2')

    # perform import
    p = appliance.import_machines()
    p.wait_for_completion()

    return vbox.find_machine(machine_name)


def list_machines():
    return vbox.machines


def stop(machine_name):
    machine = vbox.find_machine(machine_name)
    session = machine.create_session()
    try:
        session.console.power_down()
    except Exception:
        bash('kill %s' % machine.session_pid)


def start(machine_name):
    session = virtualbox.Session()
    machine = vbox.find_machine(machine_name)
    progress = machine.launch_vm_process(session, "headless", [])
    progress.wait_for_completion()


def remove(machine_name):
    machine = vbox.find_machine(machine_name)
    poweroff_vm(machine_name)
    machine.unregister(cleanup_mode=CleanupMode(4))


def poweroff_vm(vm_id):
    """
    Issues a 'poweroff' command to VirtualBox for the given VM.
    """
    logging.info("Powering off VM: %s..." % vm_id)
    bash(f'VBoxManage controlvm {vm_id} poweroff')
