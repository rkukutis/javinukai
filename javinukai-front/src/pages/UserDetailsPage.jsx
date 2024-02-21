import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import getUser from "../services/users/getUser";
import SpinnerPage from "./SpinnerPage";
import { DangerZone } from "../Components/user-management/DangerZone";
import formatTimestap from "../utils/formatTimestap";

function UserDetailsField({ fieldName, fieldValue }) {
  return (
    <section className="text py-3">
      <label className="text-lg text-slate-900">{fieldName}</label>
      <span>: </span>
      <span className="text-lg text-teal-600 text-wrap">{fieldValue}</span>
    </section>
  );
}

function UserDetailsPage() {
  const { userId } = useParams();
  const { data, isFetching, error } = useQuery({
    queryKey: ["user", userId],
    queryFn: () => getUser(userId),
  });

  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-100">
          <div className="lg:w-3/4 w-full h-fit bg-white lg:my-4 lg:p-8 rounded-md">
            <div className="lg:grid lg:grid-cols-2">
              <div>
                <h1 className="text-xl">Personal Details</h1>
                <UserDetailsField fieldName="Name" fieldValue={data?.name} />
                <UserDetailsField
                  fieldName="Surname"
                  fieldValue={data?.surname}
                />
                <UserDetailsField fieldName="Email" fieldValue={data?.email} />
                <UserDetailsField
                  fieldName="Birth year"
                  fieldValue={data?.birthYear}
                />
                <UserDetailsField
                  fieldName="Phone Number"
                  fieldValue={data?.phoneNumber}
                />
                <UserDetailsField
                  fieldName="Freelance"
                  fieldValue={data?.isFreelance ? "Yes" : "No"}
                />
                {!data?.isFreelance && (
                  <UserDetailsField
                    fieldName="Institution"
                    fieldValue={data?.institution}
                  />
                )}
              </div>
              <div className="">
                <h1 className="text-xl">Account details</h1>
                <UserDetailsField
                  fieldName="Account ID"
                  fieldValue={data?.uuid}
                />
                <UserDetailsField
                  fieldName="Creation date"
                  fieldValue={formatTimestap(data?.createdAt)}
                />
                {data?.modifiedAt && (
                  <UserDetailsField
                    fieldName="Last changed date"
                    fieldValue={formatTimestap(data?.modifiedAt)}
                  />
                )}
                <UserDetailsField
                  fieldName="User email confirmed"
                  fieldValue={data?.isEnabled ? "Yes" : "No"}
                />
                <UserDetailsField
                  fieldName="Account is locked"
                  fieldValue={data?.isNonLocked ? "No" : "Yes"}
                />
                <UserDetailsField fieldName="Role" fieldValue={data?.role} />
              </div>
            </div>
            <DangerZone userData={data} />
          </div>
        </div>
      )}
    </>
  );
}

export default UserDetailsPage;
