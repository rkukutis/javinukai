import { useQuery } from "@tanstack/react-query";
import getCompetitionRecord from "../services/records/getCompetitionRecord";
import ContestEntry from "./contest/ContestEntry";
import Button from "./Button";
import { useState } from "react";
import ImageUpload from "./ImageUpload";
import { useTranslation } from "react-i18next";

function UserSubmissionView({ contest, category, contestLimitReached }) {
  const [entryFormOpen, setEntryFormOpen] = useState(false);
  const { t } = useTranslation();
  const { data: userRecord } = useQuery({
    queryKey: ["contestRecord", contest.id, category.id],
    queryFn: () => getCompetitionRecord(contest.id, category.id),
  });

  function handleAddEntryClick() {
    setEntryFormOpen(!entryFormOpen);
  }
  return (
    <>
      {userRecord && (
        <div className="bg-slate-50 py-3 px-2 rounded">
          <div className="flex items-center space-x-2 justify-between">
            <h1 className="text-teal-500 font-bold text-xl mb-2">
              {t("UserSubmissionView.yourEntries")}{" "}
              {`( ${userRecord?.entries.length} / ${userRecord?.maxPhotos})`}
            </h1>
          </div>
          <div className="flex flex-col space-y-2">
            {userRecord?.entries.map((entry, i) => (
              <ContestEntry
                key={entry.id}
                categoryType={category.type}
                entry={entry}
                index={i}
              />
            ))}
            {userRecord?.entries.length < userRecord?.maxPhotos &&
              !contestLimitReached && (
                <Button
                  extraStyle={`${
                    entryFormOpen ? "bg-red-500 hover:bg-red-400" : ""
                  }`}
                  onClick={handleAddEntryClick}
                >
                  {entryFormOpen
                    ? t("UserSubmissionView.cancel")
                    : t("UserSubmissionView.addNewEntry")}
                </Button>
              )}
            {entryFormOpen && (
              <ImageUpload
                setEntryFormOpen={setEntryFormOpen}
                userRecord={userRecord}
                categoryInfo={category}
                contestInfo={contest}
              />
            )}
          </div>
        </div>
      )}
    </>
  );
}

export default UserSubmissionView;
