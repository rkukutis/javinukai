import Button from "./Button";
import { useTranslation } from "react-i18next";

export function JuryViewPagination({
  pagination,
  setPagination,
  availablePageNumber,
  limitObjectName,
  firstPage,
  lastPage,
}) {
  const { t } = useTranslation();
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
  function handleFilterChange(e) {
    setPagination({ ...pagination, display: e.target.value, page: 0 });
  }

  return (
    <div
      className="xl:grid xl:grid-cols-3 bg-slate-50 xl:gap-4 px-2
    shadow rounded"
    >
      <section className="flex flex-col py-2">
        <label className="mb-1">{t("JuryViewPagination.pageTitle")}</label>
        <div className="flex xl:flex-row flex-col xl:space-x-3">
          <Button
            extraStyle="w-full"
            disabled={pagination.page == 0}
            onClick={handlePreviousPage}
          >
            {t("PaginationSettings.previousPage")}
          </Button>
          <p className="text-xl py-1 px-4 rounded-full bg-white">
            {pagination.page + 1 + "/" + (availablePageNumber || 1)}
          </p>
          <Button
            extraStyle="w-full"
            disabled={
              availablePageNumber === 1 ||
              pagination.page === availablePageNumber - 1
            }
            onClick={handleNextPage}
          >
            {t("PaginationSettings.nextPage")}
          </Button>
        </div>
      </section>
      <section className="flex flex-col py-2">
        <label className="mb-1">
          {limitObjectName} {t("PaginationSettings.perPage")}
        </label>
        <select
          className="py-2 px-4 bg-white rounded-md shadow"
          onChange={handleLimitChange}
          value={pagination.limit}
        >
          <option value={10}>10 {limitObjectName}</option>
          <option value={25}>25 {limitObjectName}</option>
          <option value={50}>50 {limitObjectName}</option>
          <option value={100}>100 {limitObjectName}</option>
        </select>
      </section>
      <section className="flex flex-col py-2">
        <label className="mb-1">
          {t("JuryViewPagination.showEntriesTitle")}
        </label>
        <select
          className="py-2 px-4 bg-white rounded-md shadow"
          onChange={handleFilterChange}
          value={pagination.display}
        >
          <option value="all">
            {t("JuryViewPagination.showAll")} {limitObjectName}
          </option>
          <option value="liked">
            {t("JuryViewPagination.showLiked")} {limitObjectName}
          </option>
          <option value="unliked">
            {t("JuryViewPagination.showDisliked")} {limitObjectName}
          </option>
        </select>
      </section>
    </div>
  );
}
