const base = {
  viewBox: "0 0 24 24",
  fill: "none",
  stroke: "currentColor",
  strokeWidth: 2,
  strokeLinecap: "round",
  strokeLinejoin: "round",
  "aria-hidden": "true",
};

export function SearchIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <circle cx="11" cy="11" r="7" />
      <line x1="21" y1="21" x2="16.65" y2="16.65" />
    </svg>
  );
}

export function StarIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
    </svg>
  );
}

export function HeartIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
    </svg>
  );
}

export function DropletIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z" />
    </svg>
  );
}

export function SmileIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <circle cx="12" cy="12" r="10" />
      <path d="M8 14s1.5 2 4 2 4-2 4-2" />
      <line x1="9" y1="9" x2="9.01" y2="9" />
      <line x1="15" y1="9" x2="15.01" y2="9" />
    </svg>
  );
}

export function BrainIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M9.5 2a3.5 3.5 0 0 0-3.5 3.5v.18A3.5 3.5 0 0 0 4 9a3.5 3.5 0 0 0 1 2.45A3.5 3.5 0 0 0 4 14a3.5 3.5 0 0 0 3.5 3.5h.5v1A2.5 2.5 0 0 0 10.5 21H12V2z" />
      <path d="M14.5 2a3.5 3.5 0 0 1 3.5 3.5v.18A3.5 3.5 0 0 1 20 9a3.5 3.5 0 0 1-1 2.45A3.5 3.5 0 0 1 20 14a3.5 3.5 0 0 1-3.5 3.5H16v1A2.5 2.5 0 0 1 13.5 21H12V2z" />
    </svg>
  );
}

export function BoneIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M17 10c.7-.7 1.69-.9 2.41-.55a2.5 2.5 0 0 1 0 4.55c-.72.35-1.71.15-2.41-.55l-7 7c-.7.7-1.69.9-2.41.55a2.5 2.5 0 0 1 0-4.55c.72-.35 1.71-.15 2.41.55" />
      <path d="M14 14l-4-4" />
      <path d="M7 14c-.7.7-1.69.9-2.41.55a2.5 2.5 0 0 1 0-4.55c.72-.35 1.71-.15 2.41.55l7-7c.7-.7 1.69-.9 2.41-.55a2.5 2.5 0 0 1 0 4.55c-.72.35-1.71.15-2.41-.55" />
    </svg>
  );
}

export function EyeIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8S1 12 1 12z" />
      <circle cx="12" cy="12" r="3" />
    </svg>
  );
}

export function EyeOffIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M17.94 17.94A10.94 10.94 0 0 1 12 20c-7 0-11-8-11-8a19.77 19.77 0 0 1 4.22-5.36" />
      <path d="M9.9 4.24A10.94 10.94 0 0 1 12 4c7 0 11 8 11 8a19.86 19.86 0 0 1-3.17 4.19" />
      <path d="M14.12 14.12a3 3 0 1 1-4.24-4.24" />
      <line x1="1" y1="1" x2="23" y2="23" />
    </svg>
  );
}

export function PhoneIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.13.96.37 1.9.72 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.91.35 1.85.59 2.81.72A2 2 0 0 1 22 16.92z" />
    </svg>
  );
}

export function MailIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" />
      <polyline points="22,6 12,13 2,6" />
    </svg>
  );
}

export function MapPinIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
      <circle cx="12" cy="10" r="3" />
    </svg>
  );
}

export function ClockIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <circle cx="12" cy="12" r="10" />
      <polyline points="12 6 12 12 16 14" />
    </svg>
  );
}

export function BriefcaseIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <rect x="2" y="7" width="20" height="14" rx="2" ry="2" />
      <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" />
    </svg>
  );
}

export function CheckCircleIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
      <polyline points="22 4 12 14.01 9 11.01" />
    </svg>
  );
}

export function CalendarIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
      <line x1="16" y1="2" x2="16" y2="6" />
      <line x1="8" y1="2" x2="8" y2="6" />
      <line x1="3" y1="10" x2="21" y2="10" />
    </svg>
  );
}

export function ZapIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2" />
    </svg>
  );
}

export function UserIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
      <circle cx="12" cy="7" r="4" />
    </svg>
  );
}

export function XIcon({ className }) {
  return (
    <svg className={className} {...base}>
      <line x1="18" y1="6" x2="6" y2="18" />
      <line x1="6" y1="6" x2="18" y2="18" />
    </svg>
  );
}
