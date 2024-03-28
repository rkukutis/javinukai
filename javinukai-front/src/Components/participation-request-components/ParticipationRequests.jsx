import { useEffect, useState } from "react";
import axios from "axios";
import ParticipationRequest from "./ParticipationRequest";
import { useTranslation } from "react-i18next";
import PaginationSettings from "../PaginationSettings";
import ChangePage from "../user-management/ChangePage";

const defaultPagination = {
  page: 0,
  limit: 25,
  sortBy: "requestStatus",
  sortDesc: "true",
  searchedField: null,
};

const ParticipationRequests = () => {
  const [requestsArray, setRequestsArray] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const [firstPage, setFirstPage] = useState();
  const [lastPage, setLastPage] = useState();
  const [totalPages, setTotalPages] = useState();
  const [stateBoolean, setStateBoolean] = useState(false);
  const { t } = useTranslation();

  useEffect(() => {
    axios
      .get(
        `${import.meta.env.VITE_BACKEND}/api/v1/requests?page=${
          paginationSettings.page
        }&listSize=${paginationSettings.limit}&sortBy=${
          paginationSettings.sortBy
        }&sortDesc=${paginationSettings.sortDesc}${
          paginationSettings.searchedField
            ? "&contains=" + paginationSettings.searchedField
            : ""
        }`,
        {
          withCredentials: true,
        }
      )
      .then((data) => {
        setFirstPage(data.data.first);
        setLastPage(data.data.last);
        setTotalPages(data.data.totalPages);
        setRequestsArray(data.data.content);
        setIsLoading(false);
      })
      .catch((error) => {
        console.log(error.response);
      });
  }, [
    paginationSettings.page,
    paginationSettings.limit,
    paginationSettings.sortBy,
    paginationSettings.sortDesc,
    paginationSettings.searchedField,
    stateBoolean,
  ]);

  const changeRequestStatus = (requestId, newStatus) => {
    axios
      .patch(
        `${
          import.meta.env.VITE_BACKEND
        }/api/v1/request/${requestId}?participationStatus=${newStatus}`,
        {},
        { withCredentials: true }
      )
      .then(() => {        
        setStateBoolean(!stateBoolean);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  if (isLoading) {
    return <div>DATA IS LOADING...</div>;
  }
  console.log("request array looks like", requestsArray);

  return (
    <div className="w-full min-h-[82vh] xl:flex xl:flex-col xl:items-center bg-slate-50">
      <div className="xl:w-3/4 w-full px-2">
        <PaginationSettings
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={totalPages}
          limitObjectName={t("ParticipationRequest.contestsLimitObject")}
          sortFieldOptions={
            <>
              <option value="requestStatus">
                {t("ParticipationRequest.requestStatusOption")}
              </option>
              <option value="createdAt">
                {t("ParticipationRequest.requestDateOption")}
              </option>
              <option value="contestName">
                {t("ParticipationRequest.contestNameOption")}
              </option>
            </>
          }
          searchByFieldName={t("UserManagementPage.userSeachFieldName")}
          firstPage={firstPage}
          lastPage={lastPage}
        />

<div className="hidden xl:grid xl:grid-cols-11 px-3 py-5 font-bold text-lg text-slate-700 bg-white mt-2 rounded-md shadow">
          <p>{t("ParticipationRequest.fieldName")}</p>
          <p>{t("ParticipationRequest.fieldSurname")}</p>
          <p>{t("ParticipationRequest.fieldBirthYear")}</p>
          <p className="col-span-3">{t("ParticipationRequest.fieldEmail")}</p>
          <p className="col-span-3">{t("ParticipationRequest.fieldContestName")}</p>
          <p className="text-center">{t("ParticipationRequest.fieldAccept")}</p>
          <p className="text-center">{t("ParticipationRequest.fieldDecline")}</p>
        </div>
        {requestsArray.map((request) => {
          return (
            <ParticipationRequest
              key={request.requestId}
              request={request}
              onButtonClick={changeRequestStatus}
            />
          );
        })}
      </div>
      <div>
        <ChangePage
          pagination={paginationSettings}
          setPagination={setPaginationSettings}
          availablePageNumber={totalPages}
        />
      </div>
    </div>
  );
};
export default ParticipationRequests;
