function Table() {
  return (
    <div>
      <div className="flex flex-col">
        <div className="-m-1.5 overflow-x-auto">
          <div className="p-1.5 min-w-full inline-block align-middle">
            <div className="overflow-hidden">
              <table className="divide-y divide-gray-200">
                <thead>
                  <tr>
                    <th
                      scope="col"
                      className="px-6 py-1 text-start text-xs text-black uppercase"
                    >
                      First name
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-1 text-start text-xs text-black uppercase"
                    >
                      Last name
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-1 text-start text-xs text-black uppercase"
                    >
                      Role
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-1 text-end text-xs text-black uppercase"
                    >
                      Action
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr className="odd:bg-gray-100 even:bg-gray-300">
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Vardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Pavardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Judge
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black text-end">
                      <button
                        type="button"
                        className="inline-flex items-center gap-x-2 rounded-lg border border-transparent text-blue-600 hover:text-blue-800"
                      >
                        Edit
                      </button>
                    </td>
                  </tr>

                  <tr className="odd:bg-gray-100 even:bg-gray-300">
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Vardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Pavardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Judge
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black text-end">
                      <button
                        type="button"
                        className="inline-flex items-center gap-x-2 rounded-lg border border-transparent text-blue-600 hover:text-blue-800"
                      >
                        Edit
                      </button>
                    </td>
                  </tr>

                  <tr className="odd:bg-gray-100 even:bg-gray-300">
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Vardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Pavardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      User
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black text-end">
                      <button
                        type="button"
                        className="inline-flex items-center gap-x-2 rounded-lg border border-transparent text-blue-600 hover:text-blue-800"
                      >
                        Edit
                      </button>
                    </td>
                  </tr>

                  <tr className="odd:bg-gray-100 even:bg-gray-300">
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Vardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Pavardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Moderator
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black text-end">
                      <button
                        type="button"
                        className="inline-flex items-center gap-x-2 rounded-lg border border-transparent text-blue-600 hover:text-blue-800"
                      >
                        Edit
                      </button>
                    </td>
                  </tr>

                  <tr className="odd:bg-gray-100 even:bg-gray-300">
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Vardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      Pavardenis
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black">
                      User
                    </td>
                    <td className="px-6 py-1 whitespace-nowrap text-black text-end">
                      <button
                        type="button"
                        className="inline-flex items-center gap-x-2 rounded-lg border border-transparent text-blue-600 hover:text-blue-800"
                      >
                        Edit
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Table;
