import { Menu } from '@headlessui/react'
import { Bars3Icon } from '@heroicons/react/20/solid'
import { useTranslation } from 'react-i18next'
import { Menu } from "@headlessui/react";
import { Bars3Icon } from "@heroicons/react/20/solid";
import useUserStore from "../stores/userStore";

function classNames(...classes) {
  return classes.filter(Boolean).join(" ");
}
// eslint-disable-next-line react/prop-types
export default function DropDownMenu({ mutationFunction }) {
  const { user } = useUserStore();
  const { t } = useTranslation();
  return (
    <Menu as="div" className="relative inline-block text-left">
      <div>
        <Menu.Button className="inline-flex w-full justify-center rounded-md px-1 py-1 text-black shadow-sm hover:bg-gray-500">
          <Bars3Icon className=" h-6 w-6 text-white" aria-hidden="true" />
        </Menu.Button>
      </div>

      <Menu.Items className="absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
        <div className="py-1">
          <Menu.Item>
            {({ active }) => (
              <a
                href="#"
                className={classNames(
                  active ? "bg-gray-100 text-gray-900" : "text-gray-700",
                  "block px-4 py-2 text-sm"
                )}
              >
                Account settings
              </a>
            )}
          </Menu.Item>
          {user.role == "ADMIN" && (
            <Menu.Item>
              {({ active }) => (
                <a
                  href="/manage-users"
                  className={classNames(
                    active ? "bg-gray-100 text-gray-900" : "text-gray-700",
                    "block px-4 py-2 text-sm"
                  )}
                >
                  {t('dropdownMenu.accountSettings')}
                </a>
              )}
            </Menu.Item>
            <form method="POST" action="#">
              <Menu.Item>
                {({ active }) => (
                  <button  onClick={mutationFunction}
                    type="submit"
                    className={classNames(
                      active ? 'bg-gray-100 text-gray-900' : 'text-gray-700',
                      'block w-full px-4 py-2 text-left text-sm'
                    )}
                  >
                    {t('dropdownMenu.logout')}
                  </button>
                )}
              </Menu.Item>
            </form>
          </div>
        </Menu.Items>
    </Menu>
  );
}
