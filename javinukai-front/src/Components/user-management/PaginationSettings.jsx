import { useState } from "react";
import Button from "../Button";

export default function PaginationSettings({
  pagination,
  setPagination,
  availablePageNumber,
}) {
  const [searchedSurname, setSearchedSurname] = useState("");

  function handleNextPage() {
    if (
      availablePageNumber === 1 ||
      pagination.page === availablePageNumber - 1
    )
      return;
    setPagination({ ...pagination, page: pagination.page + 1 });
  }
  function handlePreviousPage() {
    if (pagination.page === 0) return;
    setPagination({ ...pagination, page: pagination.page - 1 });
  }
  function handleLimitChange(e) {
    e.preventDefault();
    setPagination({ ...pagination, limit: e.target.value });
  }
  function handleSortByChange(e) {
    e.preventDefault();
    setPagination({ ...pagination, sortBy: e.target.value });
  }
  function handleSortDescChange(e) {
    e.preventDefault();
    setPagination({ ...pagination, sortDesc: e.target.value });
  }
  function handleSurnameFieldChange(e) {
    setSearchedSurname(e.target.value);
  }

  function handleSurnameSearchKeydown(e) {
    if (e.key !== "Enter") return;
    handleSurnameSearchClick();
  }
  function handleSurnameSearchClick() {
    setPagination({ ...pagination, surnameContains: searchedSurname });
  }

  return (
    <div className="flex flex-col px-2 py-3 space-y-2 xl:items-end xl:justify-center xl:py-3 xl:flex-row xl:w-full xl:space-x-6">
      <section className="flex space-x-3 justify-center">
        <Button onClick={handlePreviousPage}>Previous</Button>
        <p className="text-xl py-1 px-4 rounded-full bg-white">
          {pagination.page + 1}
        </p>
        <Button extraStyle="px-8" onClick={handleNextPage}>
          Next
        </Button>
      </section>
      <section className="flex flex-col space-y-2">
        <label>Users per page</label>
        <select
          className="py-2 px-4 bg-white rounded-md"
          onChange={handleLimitChange}
          value={pagination.limit}
        >
          <option value={5}>5 users</option>
          <option value={10}>10 users</option>
          <option value={100}>100 users</option>
          <option value={200}>200 users</option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>Sort by field</label>
        <select
          className="p-2 bg-white rounded-md"
          onChange={handleSortByChange}
          value={pagination.sortBy}
        >
          <option value="name">Name</option>
          <option value="surname">Surname</option>
          <option value="email">Email</option>
          <option value="role">Role</option>
          <option value="maxTotal">Max entries per contest</option>
          <option value="maxSinglePhotos">Max singles per category</option>
          <option value="maxCollections">Max collections per category</option>
          <option value="isEnabled">Confirmed</option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>Sort direction</label>
        <select
          className="p-2 bg-white rounded-md"
          onChange={handleSortDescChange}
          value={pagination.sortDesc}
        >
          <option value="true">Descending</option>
          <option value="false">Ascending</option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>Search by surname</label>
        <div className="xl:flex-row flex xl:space-x-3 space-y-2 xl:space-y-0 flex-col">
          <input
            className="p-2 bg-white rounded-md"
            placeholder="Surname"
            onKeyDown={handleSurnameSearchKeydown}
            onChange={handleSurnameFieldChange}
            value={searchedSurname}
          />
          <Button onClick={handleSurnameSearchClick}>Search</Button>
        </div>
      </section>
    </div>
  );
}
