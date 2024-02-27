import { useState } from "react";
import Button from "../Button";
import { useTranslation } from "react-i18next";

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

  const { t } = useTranslation();

  return (
    <div className="flex flex-col px-2 py-3 space-y-2 xl:items-end xl:justify-center xl:py-3 xl:flex-row xl:w-full xl:space-x-6">
      <section className="flex space-x-3 justify-center">
        <Button onClick={handlePreviousPage}>
          {t("PaginationSettings.previousPage")}
        </Button>
        <p className="text-xl py-1 px-4 rounded-full bg-white">
          {pagination.page + 1}
        </p>
        <Button extraStyle="px-8" onClick={handleNextPage}>
          {t("PaginationSettings.nextPage")}
        </Button>
      </section>
      <section className="flex flex-col space-y-2">
        <label>{t("PaginationSettings.usersTitle")}</label>
        <select
          className="py-2 px-4 bg-white rounded-md"
          onChange={handleLimitChange}
          value={pagination.limit}
        >
          <option value={10}>10 {t("PaginationSettings.users")}</option>
          <option value={25}>25 {t("PaginationSettings.users")}</option>
          <option value={50}>50 {t("PaginationSettings.users")}</option>
          <option value={100}>100 {t("PaginationSettings.users")}</option>
        </select>
      </section>
      <section className="flex flex-col space-y-2">
        <label>{t("PaginationSettings.fieldTitle")}</label>
        <select
          className="p-2 bg-white rounded-md"
          onChange={handleSortByChange}
          value={pagination.sortBy}
        >
          <option value="name">{t("PaginationSettings.fieldName")}</option>
          <option value="surname">
            {t("PaginationSettings.fieldSurname")}
          </option>
          <option value="email">{t("PaginationSettings.fieldEmail")}</option>
          <option value="role">{t("PaginationSettings.fieldRole")}</option>
          <option value="maxTotal">
            {t("PaginationSettings.fieldMaxTotalEntries")}
          </option>
          <option value="maxSinglePhotos">
            {t("PaginationSettings.fieldMaxSingles")}
          </option>
          <option value="maxCollections">
            {t("PaginationSettings.fieldMaxCollections")}
          </option>
          <option value="isEnabled">
            {t("PaginationSettings.fieldIsEnabled")}
          </option>
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
        <label>{t("PaginationSettings.searchTitle")}</label>
        <div className="lg:flex-row flex lg:space-x-3 space-y-2 lg:space-y-0 flex-col">
          <input
            className="p-2 bg-white rounded-md"
            placeholder={t("PaginationSettings.searchPlaceholder")}
            onKeyDown={handleSurnameSearchKeydown}
            onChange={handleSurnameFieldChange}
            value={searchedSurname}
          />
          <Button onClick={handleSurnameSearchClick}>
            {t("PaginationSettings.searchButton")}
          </Button>
        </div>
      </section>
    </div>
  );
}
