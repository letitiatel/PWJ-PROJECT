import { useMemo, useState } from "react";
import {
  addMonths,
  endOfMonth,
  endOfWeek,
  format,
  isBefore,
  isSameDay,
  isSameMonth,
  startOfDay,
  startOfMonth,
  startOfWeek,
  addDays,
} from "date-fns";
import { ro } from "date-fns/locale";

export default function Calendar({ selected, onSelect, disabledBefore }) {
  const [cursor, setCursor] = useState(startOfMonth(selected || new Date()));

  const today = startOfDay(new Date());
  const min = disabledBefore ? startOfDay(disabledBefore) : today;

  const days = useMemo(() => {
    const start = startOfWeek(startOfMonth(cursor), { weekStartsOn: 1 });
    const end = endOfWeek(endOfMonth(cursor), { weekStartsOn: 1 });
    const out = [];
    let d = start;
    while (d <= end) {
      out.push(d);
      d = addDays(d, 1);
    }
    return out;
  }, [cursor]);

  const weekdayLabels = ["L", "M", "M", "J", "V", "S", "D"];

  return (
    <div className="bg-white rounded-xl border border-slate-200 p-4">
      <div className="flex items-center justify-between">
        <button
          type="button"
          onClick={() => setCursor(addMonths(cursor, -1))}
          className="px-2.5 py-1 rounded-md hover:bg-slate-100 text-slate-600"
          aria-label="Luna anterioară"
        >
          ◀
        </button>
        <div className="font-semibold text-slate-800 capitalize">
          {format(cursor, "LLLL yyyy", { locale: ro })}
        </div>
        <button
          type="button"
          onClick={() => setCursor(addMonths(cursor, 1))}
          className="px-2.5 py-1 rounded-md hover:bg-slate-100 text-slate-600"
          aria-label="Luna următoare"
        >
          ▶
        </button>
      </div>

      <div className="mt-4 grid grid-cols-7 text-center text-xs font-medium text-slate-500">
        {weekdayLabels.map((w, i) => (
          <div key={i} className="py-2">
            {w}
          </div>
        ))}
      </div>

      <div className="grid grid-cols-7 gap-1">
        {days.map((d) => {
          const inMonth = isSameMonth(d, cursor);
          const isSelected = selected && isSameDay(d, selected);
          const isDisabled = isBefore(d, min);
          const base =
            "aspect-square rounded-lg text-sm flex items-center justify-center transition";
          let cls = `${base} `;
          if (isDisabled)
            cls += "text-slate-300 cursor-not-allowed";
          else if (isSelected)
            cls += "bg-brand-600 text-white font-bold shadow";
          else if (inMonth)
            cls += "text-slate-700 hover:bg-brand-50";
          else cls += "text-slate-400 hover:bg-slate-50";
          return (
            <button
              key={d.toISOString()}
              type="button"
              disabled={isDisabled}
              onClick={() => onSelect(d)}
              className={cls}
            >
              {format(d, "d")}
            </button>
          );
        })}
      </div>
    </div>
  );
}
