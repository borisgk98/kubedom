import configparser
import uuid
import sys
from kubedom.const import CONFIG_PATH

__SECTION = 'configuration'
__config = configparser.ConfigParser()
__config.read(CONFIG_PATH)
if not __config.has_section(__SECTION) or not __config.has_option(__SECTION, 'Token'):
    print("Incorrect config")
    sys.exit(0)
TOKEN = __config[__SECTION]['Token']
NODE_UUID = None
if not __config.has_option(__SECTION, 'Node_uuid'):
    NODE_UUID = uuid.uuid4()
    __config.set(__SECTION, 'Node_uuid', str(NODE_UUID))
    with open(CONFIG_PATH, 'w') as configfile:
        __config.write(configfile)
else:
    NODE_UUID = __config.get(__SECTION, 'Node_uuid')