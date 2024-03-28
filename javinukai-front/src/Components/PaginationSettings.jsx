import { useState } from "react";
import Button from "./Button";
import { useTranslation } from "react-i18next";

export default function PaginationSettings({
  pagination,
  setPagination,
  availablePageNumber,
  limitObjectName,
  sortFieldOptions,
  searchByFieldName,
  firstPage,
  lastPage,
}) {
  const [searchedField, setSearchedField] = useState("");

  function handleNextPage() {
    if (lastPage) return;
    setPagination({ ...pagination, page: pagination.page + 1 });
  }
  function handlePreviousPage() {
    if (firstPage) return;
    setPagination({ ...pagination, page: pagination.page - 1 });
  }
  function handleLimitChange(e) {
    setPagination({ ...pagination, limit: e.target.value, page: 0 });
  }
  function handleSortByChange(e) {
    setPagination({ ...pagination, sortBy: e.target.value });
  }
  function handleSortDescChange(e) {
    setPagination({ ...pagination, sortDesc: e.target.value });
  }
  function handleSearchFieldChange(e) {
    setSearchedField(e.target.value);
  }

  function handleSearchFieldKeydown(e) {
    if (e.key !== "Enter") return;
    handleSearchClick();
  }
  function handleSearchClick() {
    setPagination({ ...pagination, searchedField });
  }

  const { t } = useTranslation();
  return (
    <div className="flex flex-col px-2 py-3 space-y-2 xl:grid xl:grid-cols-4 xl:grid-rows-2 xl:gap-2">
      <section className="flex space-x-3 justify-center xl:col-span-4 xl:h-fit xl:self-end">
        <Button disabled={pagination.page == 0} onClick={handlePreviousPage}>
          {t("PaginationSettings.previousPage")}
        </Button>
        <p className="text-xl py-1 px-4 rounded-full bg-white">
          {pagination.page + 1 + "/" + (availablePageNumber || pagination.page)}
        </p>
        <Button
          disabled={
            availablePageNumber === 1 ||
            pagination.page === availablePageNumber - 1
          }
          extraStyle="px-8"
          onClick={handleNextPage}
        >
          {t("PaginationSettings.nextPage")}
        </Button>
      </section>
      <section className="flex flex-col space-y-2">
        <label>
          {limitObjectName} {t("PaginationSettings.perPage")}
        </label>
        <select
          className="py-2 px-4 bg-white rounded-md"
          onChange={handleLimitChange}
          value={pagination.limit}
        >
          <option value={10}>10 {limitObjectName}</option>
          <option value={25}>25 {limitObjectName}</option>
          <option value={50}>50 {limitObjectName}</option>
          <option value={100}>100 {limitObjectName}</option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>{t("PaginationSettings.fieldTitle")}</label>
        <select
          className="p-2 bg-white rounded-md"
          onChange={handleSortByChange}
          value={pagination.sortBy}
        >
          {sortFieldOptions}
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>{t("PaginationSettings.sortTitle")}</label>
        <select
          className="p-2 bg-white rounded-md"
          onChange={handleSortDescChange}
          value={pagination.sortDesc}
        >
          <option value="true">{t("PaginationSettings.sortDescending")}</option>
          <option value="false">{t("PaginationSettings.sortAscending")}</option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>
          {t("PaginationSettings.searchTitle")} {searchByFieldName}
        </label>
        <div className="lg:flex-row flex lg:space-x-3 space-y-2 lg:space-y-0 flex-col">
          <input
            className="p-2 bg-white rounded-md w-full"
            placeholder={`${t(
              "PaginationSettings.placeholderField"
            )} ${searchByFieldName}`}
            onKeyDown={handleSearchFieldKeydown}
            onChange={handleSearchFieldChange}
            value={searchedField}
          />
          <Button onClick={handleSearchClick}>
            {t("PaginationSettings.searchButton")}
          </Button>
        </div>
      </section>
    </div>
  );
}
