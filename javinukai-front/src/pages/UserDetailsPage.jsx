import { useQuery } from "@tanstack/react-query";
import { useNavigate, useParams } from "react-router-dom";
import getUser from "../services/users/getUser";
import SpinnerPage from "./SpinnerPage";
import { DangerZone } from "../Components/user-management/DangerZone";
import formatTimestap from "../utils/formatTimestap";
import Button from "../Components/Button";
import { useTranslation } from "react-i18next";

function UserDetailsField({ fieldName, fieldValue, valueIsRed }) {
  return (
    <section className="text py-3">
      <label className={`text-lg text-slate-900`}>{fieldName}</label>
      <span>: </span>
      <span
        className={`text-lg text-wrap ${
          valueIsRed ? "text-red-500 font-bold" : "text-teal-600"
        }`}
      >
        {fieldValue}
      </span>
    </section>
  );
}

function UserDetailsPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { userId } = useParams();
  const { data, isFetching } = useQuery({
    queryKey: ["user", userId],
    queryFn: () => getUser(userId),
  });

  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-100">
          <div className="lg:w-3/4 w-full h-fit bg-white shadow-md lg:my-4 p-8 rounded-md">
            <article className="lg:grid lg:grid-cols-2 flex flex-col space-y-4 lg:space-y-0 pb-4">
              <section>
                <h1 className="text-2xl">
                  {t("UserDetailsPage.personalTitle")}
                </h1>
                <UserDetailsField
                  fieldName={t("UserDetailsPage.personalName")}
                  fieldValue={data?.name}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.personalSurname")}
                  fieldValue={data?.surname}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.personalEmail")}
                  fieldValue={data?.email}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.personalEmail")}
                  fieldValue={data?.email}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.personalBirthYear")}
                  fieldValue={data?.birthYear}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.personalPhoneNumber")}
                  fieldValue={data?.phoneNumber}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.personalFreelance")}
                  fieldValue={
                    data?.isFreelance
                      ? t("UserDetailsPage.isTrue")
                      : t("UserDetailsPage.isFalse")
                  }
                />
                {!data?.isFreelance && (
                  <UserDetailsField
                    fieldName={t("UserDetailsPage.personalInstitution")}
                    fieldValue={data?.institution}
                  />
                )}
              </section>
              <section className="">
                <h1 className="text-2xl">
                  {t("UserDetailsPage.accountTitle")}
                </h1>
                <UserDetailsField
                  fieldName={t("UserDetailsPage.accountID")}
                  fieldValue={data?.id}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.accountCreationDate")}
                  fieldValue={formatTimestap(data?.createdAt)}
                />
                {data?.modifiedAt && (
                  <UserDetailsField
                    fieldName={t("UserDetailsPage.accountLastModifiedDate")}
                    fieldValue={formatTimestap(data?.modifiedAt)}
                  />
                )}
                <UserDetailsField
                  fieldName={t("UserDetailsPage.accountEmailConfirmed")}
                  fieldValue={
                    data?.isEnabled
                      ? t("UserDetailsPage.isTrue")
                      : t("UserDetailsPage.isFalse")
                  }
                  valueIsRed={!data?.isEnabled}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.accountIsLocked")}
                  fieldValue={
                    data?.isNonLocked
                      ? t("UserDetailsPage.isFalse")
                      : t("UserDetailsPage.isTrue")
                  }
                  valueIsRed={!data?.isNonLocked}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.accountRole")}
                  fieldValue={t(`roles.${data?.role}`) || data?.role}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.accountMaxPhotosContest")}
                  fieldValue={data?.maxTotal}
                />
                <UserDetailsField
                  fieldName={t("UserDetailsPage.accountMaxSinglesCategories")}
                  fieldValue={data?.maxSinglePhotos}
                />
                <UserDetailsField
                  fieldName={t(
                    "UserDetailsPage.accountMaxCollectionCategories"
                  )}
                  fieldValue={data?.maxCollections}
                />
              </section>
            </article>
            <DangerZone userData={data} />
            <Button
              onClick={() => navigate("/manage-users")}
              extraStyle="text-lg mt-2 w-full lg:w-fit"
            >
              {t("UserDetailsPage.backButton")}
            </Button>
          </div>
        </div>
      )}
    </>
  );
}

export default UserDetailsPage;
