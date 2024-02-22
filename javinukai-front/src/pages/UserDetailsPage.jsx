import { useQuery } from "@tanstack/react-query";
import { useNavigate, useParams } from "react-router-dom";
import getUser from "../services/users/getUser";
import SpinnerPage from "./SpinnerPage";
import { DangerZone } from "../Components/user-management/DangerZone";
import formatTimestap from "../utils/formatTimestap";
import Button from "../Components/Button";

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
                <h1 className="text-2xl">Personal Details</h1>
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
              </section>
              <section className="">
                <h1 className="text-2xl">Account details</h1>
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
                <UserDetailsField
                  fieldName="Max photos per contest"
                  fieldValue={data?.maxTotal}
                />
                <UserDetailsField
                  fieldName="Max photos for Singles categories"
                  fieldValue={data?.maxSinglePhotos}
                />
                <UserDetailsField
                  fieldName="Max photos for Collections categories"
                  fieldValue={data?.maxCollections}
                />
              </section>
            </article>
            <DangerZone userData={data} />
            <Button
              onClick={() => navigate("/manage-users")}
              extraStyle="text-lg mt-2 w-full lg:w-fit"
            >
              Back
            </Button>
          </div>
        </div>
      )}
    </>
  );
}

export default UserDetailsPage;
